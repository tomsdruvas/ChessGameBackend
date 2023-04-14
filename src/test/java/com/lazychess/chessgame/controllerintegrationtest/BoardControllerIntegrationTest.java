package com.lazychess.chessgame.controllerintegrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.applicationuser.ApplicationUserRepository;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.EmptyPiece;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.config.SquareListConverter;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.repository.entity.PlayersDao;

import wiremock.com.fasterxml.jackson.core.JsonProcessingException;
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

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private ApplicationUser applicationUser;
    private ApplicationUser applicationUser2;
    private BoardDao savedBoardDao;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_roles", "chess_game_board", "chess_game_user");
    }

    @Test
    void existentUserShouldBeAbleToCreateAChessSession() throws Exception {
        createUserOne();
        MvcResult mvcResult = mockMvc.perform(post("/board")
                .header("Authorization", "Bearer " + getPlayerOneAccessToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1")).andReturn();

        Square[][] squaresFromResponseBody = getSquaresFromResponseBody(mvcResult);
        Square[][] expected = new Board().getSquares();
        assertArrayEquals(squaresFromResponseBody, expected);
    }

    @Test
    void existentUserShouldBeAbleToJoinAChessSession() throws Exception {
        createABoardWithOneUsersAndReturnBoardId();
        MvcResult mvcResult = mockMvc.perform(post("/add-player-two-board/" + savedBoardDao.getId())
                .header("Authorization", "Bearer " + getPlayerTwoAccessToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andReturn();

        Square[][] squaresFromResponseBody = getSquaresFromResponseBody(mvcResult);
        Square[][] expected = new Board().getSquares();
        assertArrayEquals(squaresFromResponseBody, expected);
    }

    @Test
    void playerOneOfTheBoardShouldBeAbleToMakeAMove() throws Exception {
        createABoardWithTwoUsers();
        MvcResult mvcResult = mockMvc.perform(post("/make-a-move/" + savedBoardDao.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerOneAccessToken())
                .content(getMoveJsonRequestBody(6, 3, 4, 3)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user2"))
            .andReturn();

        Square[][] squaresFromResponseBody = getSquaresFromResponseBody(mvcResult);

        assertThat(squaresFromResponseBody).satisfies(squares1 -> {
            assertThat(squares1[6][3].getPiece()).isExactlyInstanceOf(EmptyPiece.class);
            assertThat(squares1[4][3].getPiece()).isExactlyInstanceOf(Pawn.class);
        });

        squaresFromResponseBody[6][3] = null;
        squaresFromResponseBody[4][3] = null;
        Square[][] squares = new Board().getSquares();
        squares[6][3] = null;
        squares[4][3] = null;
        assertArrayEquals(squaresFromResponseBody, squares);
    }

    @Test
    void playerTwoOfTheBoardShouldBeAbleToMakeAMove() throws Exception {
        createACustomBoardWithTwoUsersForUserTwoMove(
            List.of(
                new ChessMoveDto(6,3,4,3)
            )
        );
        MvcResult mvcResult = mockMvc.perform(post("/make-a-move/" + savedBoardDao.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerTwoAccessToken())
                .content(getMoveJsonRequestBody(1, 2, 2, 2)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user1"))
            .andReturn();

        Square[][] squaresFromResponseBody = getSquaresFromResponseBody(mvcResult);

        assertThat(squaresFromResponseBody).satisfies(squares1 -> {
            assertThat(squares1[1][2].getPiece()).isExactlyInstanceOf(EmptyPiece.class);
            assertThat(squares1[2][2].getPiece()).isExactlyInstanceOf(Pawn.class);
        });

        squaresFromResponseBody[6][3] = null;
        squaresFromResponseBody[4][3] = null;

        squaresFromResponseBody[1][2] = null;
        squaresFromResponseBody[2][2] = null;
        Square[][] squares = new Board().getSquares();
        squares[6][3] = null;
        squares[4][3] = null;

        squares[1][2] = null;
        squares[2][2] = null;
        assertArrayEquals(squaresFromResponseBody, squares);
    }

    @Test
    void playerTwoOfTheBoardShouldBeAbleToMakeAMoveAndTheResultShouldBeCheckmate() throws Exception {
        createACustomBoardWithTwoUsersForUserOneMove(
            List.of(
                new ChessMoveDto(6,3,4,3),
                new ChessMoveDto(1,2,2,2),
                new ChessMoveDto(6,4,4,4),
                new ChessMoveDto(1,1,3,1)
            )
        );
        MvcResult mvcResult = mockMvc.perform(post("/make-a-move/" + savedBoardDao.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerOneAccessToken())
                .content(getMoveJsonRequestBody(7, 4, 3, 0)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user2"))
            .andExpect(jsonPath("$.WinnerUsername").value("test_user1"))
            .andExpect(jsonPath("$.GameState").value("CHECKMATE"))
            .andReturn();

        Square[][] squaresFromResponseBody = getSquaresFromResponseBody(mvcResult);

        assertThat(squaresFromResponseBody).satisfies(squares -> {
            assertThat(squares[7][4].getPiece()).isExactlyInstanceOf(EmptyPiece.class);
            assertThat(squares[3][0].getPiece()).isExactlyInstanceOf(Queen.class);
        });
    }

    private Square[][] getSquaresFromResponseBody(MvcResult mvcResult) throws UnsupportedEncodingException {
        String response = mvcResult.getResponse().getContentAsString();
        String squaresString = JsonPath.parse(response).read("$['Squares']").toString();
        return new SquareListConverter().convertToEntityAttribute(squaresString);
    }

    private String getMoveJsonRequestBody(int currentRow, int currentColumn, int newRow, int newColumn) throws JsonProcessingException {
        ChessMoveDto chessMoveDto = new ChessMoveDto(currentRow, currentColumn, newRow, newColumn);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(chessMoveDto);
    }

    private void createABoardWithTwoUsers() {
        createUserOne();
        createUserTwo();

        Board board = new Board();
        BoardDao boardDao = new BoardDao();
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        boardDao.setSquares(board.getSquares());
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(applicationUser.getId());
        playersDao.setPlayerTwoAppUserId(applicationUser2.getId());
        playersDao.setPlayerOneAppUsername(applicationUser.getUsername());
        playersDao.setPlayerTwoAppUsername(applicationUser2.getUsername());
        playersDao.setActivePlayerUsername(applicationUser.getUsername());
        boardDao.setPlayersDao(playersDao);
        savedBoardDao = boardRepository.saveAndFlush(boardDao);
    }

    private void createABoardWithOneUsersAndReturnBoardId() {
        createUserOne();
        createUserTwo();

        Board board = new Board();
        BoardDao boardDao = new BoardDao();
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        boardDao.setSquares(board.getSquares());
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(applicationUser.getId());
        playersDao.setPlayerOneAppUsername(applicationUser.getUsername());
        playersDao.setActivePlayerUsername(applicationUser.getUsername());
        boardDao.setPlayersDao(playersDao);
        savedBoardDao = boardRepository.saveAndFlush(boardDao);
    }

    private void createACustomBoardWithTwoUsersForUserOneMove(List<ChessMoveDto> chessMoveDtoList) {
        createUserOne();
        createUserTwo();

        Board board = new Board(chessMoveDtoList);
        board.setCurrentPlayerColourState("white");
        BoardDao boardDao = new BoardDao();
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        boardDao.setSquares(board.getSquares());
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(applicationUser.getId());
        playersDao.setPlayerTwoAppUserId(applicationUser2.getId());
        playersDao.setPlayerOneAppUsername(applicationUser.getUsername());
        playersDao.setPlayerTwoAppUsername(applicationUser2.getUsername());
        playersDao.setActivePlayerUsername(applicationUser.getUsername());
        boardDao.setPlayersDao(playersDao);
        savedBoardDao = boardRepository.saveAndFlush(boardDao);
    }

    private void createACustomBoardWithTwoUsersForUserTwoMove(List<ChessMoveDto> chessMoveDtoList) {
        createUserOne();
        createUserTwo();

        Board board = new Board(chessMoveDtoList);
        board.setCurrentPlayerColourState("black");
        BoardDao boardDao = new BoardDao();
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        boardDao.setSquares(board.getSquares());
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(applicationUser.getId());
        playersDao.setPlayerTwoAppUserId(applicationUser2.getId());
        playersDao.setPlayerOneAppUsername(applicationUser.getUsername());
        playersDao.setPlayerTwoAppUsername(applicationUser2.getUsername());
        playersDao.setActivePlayerUsername(applicationUser2.getUsername());
        boardDao.setPlayersDao(playersDao);
        savedBoardDao = boardRepository.saveAndFlush(boardDao);
    }

    private void createUserOne() {
        ApplicationUser testUser1 = new ApplicationUser("test-user-data-id1", "test_user1", "$2a$10$0gJXCjduBg9FgnvFm8E7I.VWOejHp7J/Xpa/DMLu9xENiOm61FUxS");
        applicationUser = applicationUserRepository.saveAndFlush(testUser1);
    }

    private void createUserTwo() {
        ApplicationUser testUser2 = new ApplicationUser("test-user-data-id2", "test_user2", "$2a$10$0gJXCjduBg9FgnvFm8E7I.VWOejHp7J/Xpa/DMLu9xENiOm61FUxS");
        applicationUser2 = applicationUserRepository.saveAndFlush(testUser2);
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
