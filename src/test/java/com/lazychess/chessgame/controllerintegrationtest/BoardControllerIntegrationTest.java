package com.lazychess.chessgame.controllerintegrationtest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.lazychess.chessgame.controllerintegrationtest.data.CreateBoardTestData;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class BoardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_roles", "chess_game_board", "chess_game_user");
    }

    @Test
    void shouldCreateChessBoard() throws Exception {
        CreateBoardTestData createBoardTestData = new CreateBoardTestData();

        MvcResult actual = mockMvc
            .perform(request(createBoardTestData.httpMethod(), createBoardTestData.endpointUrl()))
            .andReturn();
    }

    @Test
    @Sql("classpath:sql/usersAndBoard.sql")
    void existentUserShouldBeAbleToCreateAChessSession() throws Exception {

        mockMvc.perform(post("/board")
                .header("Authorization", "Bearer " + getPlayerOneAccessToken()))
            .andExpect(status().isCreated());
    }

    @Test
    @Sql("classpath:sql/usersAndBoard.sql")
    void existentUserShouldBeAbleToJoinAChessSession() throws Exception {

        mockMvc.perform(post("/add-player-two-board/test-data-id01")
                .header("Authorization", "Bearer " + getPlayerTwoAccessToken()))
            .andExpect(status().isCreated());
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
