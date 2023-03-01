package com.lazychess.chessgame.controllerintegrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.EmptyPiece;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.config.SquareListConverter;
import com.lazychess.chessgame.dto.ChessMoveDto;

import wiremock.com.fasterxml.jackson.core.JsonProcessingException;
import wiremock.com.fasterxml.jackson.databind.DeserializationFeature;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.databind.ObjectWriter;
import wiremock.com.fasterxml.jackson.databind.SerializationFeature;
import wiremock.com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class BoardControllerIntegrationTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_roles", "chess_game_board", "chess_game_user");
    }

    @Test
    @Sql("classpath:sql/usersAndBoard.sql")
    void existentUserShouldBeAbleToCreateAChessSession() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/board")
                .header("Authorization", "Bearer " + getPlayerOneAccessToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1")).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Board board = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(response, Board.class);

        assertThat(board).usingRecursiveComparison().isEqualTo(new Board());
    }

    @Test
    @Sql("classpath:sql/usersAndBoard.sql")
    void existentUserShouldBeAbleToJoinAChessSession() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/add-player-two-board/test-data-id01")
                .header("Authorization", "Bearer " + getPlayerTwoAccessToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andReturn();

        Square[][] squaresFromResponseBody = getSquaresFromResponseBody(mvcResult);

        assertThat(squaresFromResponseBody).isEqualTo(new Board().getSquares());
    }

    @Test
    @Sql("classpath:sql/movesForPlayerOne.sql")
    void playerOneOfTheBoardShouldBeAbleToMakeAMove() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/make-a-move/test-data-id02")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerOneAccessToken())
                .content(getMoveJsonRequestBody(6, 5, 4, 5)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user2"))
            .andReturn();

        Square[][] squares = getSquaresFromResponseBody(mvcResult);

        assertThat(squares).satisfies(squares1 -> {
            assertThat(squares1[6][5].getPiece()).isExactlyInstanceOf(EmptyPiece.class);
            assertThat(squares1[4][5].getPiece()).isExactlyInstanceOf(Pawn.class);
        });
    }

    @Test
    @Sql("classpath:sql/movesForPlayerTwo.sql")
    void playerTwoOfTheBoardShouldBeAbleToMakeAMove() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/make-a-move/test-data-id02")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerTwoAccessToken())
                .content(getMoveJsonRequestBody(1, 4, 2, 4)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user1"))
            .andReturn();

        Square[][] squares = getSquaresFromResponseBody(mvcResult);

        assertThat(squares).satisfies(squares1 -> {
            assertThat(squares1[1][4].getPiece()).isExactlyInstanceOf(EmptyPiece.class);
            assertThat(squares1[2][4].getPiece()).isExactlyInstanceOf(Pawn.class);
        });
    }

    @Test
    @Sql("classpath:sql/movesForPlayerTwoCheckmate.sql")
    void playerTwoOfTheBoardShouldBeAbleToMakeAMoveAndTherResultShouldBeCheckmate() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/make-a-move/test-data-id02")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerTwoAccessToken())
                .content(getMoveJsonRequestBody(0, 3, 4, 7)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user1"))
            .andExpect(jsonPath("$.WinnerUsername").value("test_user2"))
            .andExpect(jsonPath("$.GameState").value("CHECKMATE"))
            .andReturn();

        Square[][] squares = getSquaresFromResponseBody(mvcResult);

        assertThat(squares).satisfies(squares1 -> {
            assertThat(squares1[0][3].getPiece()).isExactlyInstanceOf(EmptyPiece.class);
            assertThat(squares1[4][7].getPiece()).isExactlyInstanceOf(Queen.class);
        });
    }

    private static Square[][] getSquaresFromResponseBody(MvcResult mvcResult) throws UnsupportedEncodingException {
        String response = mvcResult.getResponse().getContentAsString();
        String squaresString = JsonPath.parse(response).read("$['Squares']").toString();
        return new SquareListConverter().convertToEntityAttribute(squaresString);
    }

    private static String getMoveJsonRequestBody(int currentRow, int currentColumn, int newRow, int newColumn) throws JsonProcessingException {
        ChessMoveDto chessMoveDto = new ChessMoveDto(currentRow, currentColumn, newRow, newColumn);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(chessMoveDto);
    }


    private String getPlayerOneAccessToken() throws Exception {
        String username = "test_user1";
        String password = "admin";

        MvcResult result = mockMvc.perform(post("/token")
                .with(httpBasic(username, password)))
            .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response).get("access_token").toString();
    }

    private String getPlayerTwoAccessToken() throws Exception {
        String username = "test_user2";
        String password = "admin";

        MvcResult result = mockMvc.perform(post("/token")
                .with(httpBasic(username, password)))
            .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response).get("access_token").toString();
    }
}
