package com.lazychess.chessgame.controller;

import static com.lazychess.chessgame.controller.TestConstants.LOGIN_PATH;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.repository.ApplicationUserRepository;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.entity.ApplicationUser;
import com.lazychess.chessgame.repository.entity.BoardEntity;
import com.lazychess.chessgame.repository.entity.PlayersEntity;
import com.lazychess.chessgame.repository.mapper.BoardEntityMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)

@AutoConfigureMockMvc
@ContextConfiguration
//@WebAppConfiguration
public class WebSocketEndpointIntegrationTest {

//    @MockBean
//    private static TopicSubscriptionInterceptor topicSubscriptionInterceptor;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardEntityMapper boardEntityMapper;


    @LocalServerPort
    private Integer port;

    private static final String SUBSCRIBE_BOARD_ENDPOINT = "/topic/game-progress/";
    private static String URL;

    private ApplicationUser applicationUser;
    private ApplicationUser applicationUser2;
    private BoardEntity savedBoardEntity;

    private MvcResult mvcResult;

    @BeforeAll
    public static void setUp() {
    }

    @Test
    void shouldJoinChessgameWebsocket() throws Exception {
        createABoardWithTwoUsers();

        URL = "ws://localhost:" + port + "/ws?access_token=" + getPlayerOneAccessToken();

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSession stompSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_BOARD_ENDPOINT + savedBoardEntity.getId(), new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        stompSession.send("/topic/game-progress/" + savedBoardEntity.getId(), "Test Message");

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> assertEquals("Test Message", blockingQueue.poll()));


    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
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

        mvcResult = mockMvc.perform(post(LOGIN_PATH)
                .with(httpBasic(username, password)))
            .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response).get("accessToken").toString();
    }

    private void createABoardWithTwoUsers() {
        createUserOne();
        createUserTwo();

        Board board = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(board);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(applicationUser.getId());
        playersEntity.setPlayerOneAppUsername(applicationUser.getUsername());
        playersEntity.setActivePlayerUsername(applicationUser.getUsername());
        boardEntity.setPlayersEntity(playersEntity);
        savedBoardEntity = boardRepository.saveAndFlush(boardEntity);
    }
}
