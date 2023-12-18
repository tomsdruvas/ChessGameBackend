package com.lazychess.chessgame.controller;

import static com.lazychess.chessgame.controller.TestConstants.ADD_PLAYER_TWO_TO_BOARD_PATH;
import static com.lazychess.chessgame.controller.TestConstants.LOGIN_PATH;
import static com.lazychess.chessgame.controller.TestConstants.MAKE_A_MOVE_PATH;
import static com.lazychess.chessgame.controller.TestConstants.PROMOTE_PAWN_PATH;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testUtil.GenericComparators.DYNAMIC_FIELDS;
import static testUtil.GenericComparators.notNullComparator;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.config.ChessGameWebsocketMessage;
import com.lazychess.chessgame.config.WebsocketMessageType;
import com.lazychess.chessgame.dto.ChessMoveRequest;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.repository.ApplicationUserRepository;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.entity.ApplicationUser;
import com.lazychess.chessgame.repository.entity.BoardEntity;
import com.lazychess.chessgame.repository.entity.PlayersEntity;
import com.lazychess.chessgame.repository.mapper.BoardEntityMapper;

import jakarta.annotation.Nonnull;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration
public class WebSocketEndpointIntegrationTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardEntityMapper boardEntityMapper;
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @LocalServerPort
    private Integer port;

    private static final String SUBSCRIBE_BOARD_ENDPOINT = "/topic/game-progress/";
    private static String URL;

    private ApplicationUser applicationUser;
    private ApplicationUser applicationUser2;
    private BoardEntity savedBoardEntity;
    private JsonObjectBoardResponse jsonObjectBoardResponse;
    private WebSocketStompClient stompClient;
    private BlockingQueue<ChessGameWebsocketMessage> blockingQueue;
    private StompSession stompSession;
    private SimpSession simpSession;
    private MvcResult mvcResult;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "refresh_token", "user_roles", "chess_game_board", "chess_game_user");
    }

    @BeforeAll
    public static void setUp() {
    }

    @Test
    void shouldReceiveMessagesFromWebSocketSession() throws Exception {
        givenWeHaveABoardWithOneUser();
        andWeHaveWsUrlWithAccessTokenForUser(getPlayerOneAccessToken());
        andWeHaveAStompClient();
        andWeSetBlockingQueueAndConverterType();
        andWeAsyncConnectToWebSocket();
        andWeSubscribeToChessBoardAndExpectAStringPayload();
        whenWeSendMessageUsingStompSession();
        andWeFindSimpSessionForUser(applicationUser);

        await()
            .atMost(2, SECONDS)
            .untilAsserted(() -> {
                ChessGameWebsocketMessage chessGameWebsocketMessage = blockingQueue.take();
                assertEquals(WebsocketMessageType.NOTIFICATION, chessGameWebsocketMessage.getType());
                assertEquals("Test Message", chessGameWebsocketMessage.getMessage());
                assertEquals(1, simpUserRegistry.getUserCount());
                assertEquals(1, simpSession.getSubscriptions().size());
            });
    }

    @Test
    void shouldReceiveMessageAboutPlayerTwoJoining() throws Exception {
        givenWeHaveABoardWithOneUser();
        andWeHaveWsUrlWithAccessTokenForUser(getPlayerOneAccessToken());
        andWeHaveAStompClient();
        andWeSetBlockingQueueAndConverterType();
        andWeAsyncConnectToWebSocket();
        andWeSubscribeToChessBoardAndExpectAStringPayload();
        andWeFindSimpSessionForUser(applicationUser);
        andWeHaveUserTwo();
        whenPlayerTwoJoinsGame();

        await()
            .atMost(2, SECONDS)
            .untilAsserted(() -> {
                ChessGameWebsocketMessage chessGameWebsocketMessage = blockingQueue.take();
                assertEquals(WebsocketMessageType.NOTIFICATION, chessGameWebsocketMessage.getType());
                assertEquals("test_user2 has joined the game", chessGameWebsocketMessage.getMessage());
                assertEquals(1, simpSession.getSubscriptions().size());
                assertEquals(1, simpUserRegistry.getUserCount());
            });
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void playerTwoShouldNotBeAbleToJoinWhenNotPartOfChessGame() throws Exception {
        givenWeHaveABoardWithOneUser();
        andWeHaveUserTwo();
        andWeHaveWsUrlWithAccessTokenForUser(getPlayerTwoAccessToken());
        andWeHaveAStompClient();
        andWeSetBlockingQueueAndConverterType();
        andWeAsyncConnectToWebSocket();
        andWeSubscribeToChessBoardAndExpectAStringPayload();
        andWeFindSimpSessionForUser(applicationUser2);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> {
                assertEquals(0, simpSession.getSubscriptions().size());
                assertEquals(0, simpUserRegistry.getUserCount());
            });
    }

    @Test
    void shouldReceiveMessageAboutPlayerOneMakingAMove() throws Exception {
        givenWeHaveABoardWithTwoUsers();
        andWeHaveWsUrlWithAccessTokenForUser(getPlayerOneAccessToken());
        andWeHaveAStompClient();
        andWeSetBlockingQueueAndConverterType();
        andWeAsyncConnectToWebSocket();
        andWeSubscribeToChessBoardAndExpectAStringPayload();
        andWeFindSimpSessionForUser(applicationUser);
        whenPlayerOneMakeAMove();
        await()
            .atMost(5, SECONDS)
            .untilAsserted(() -> {
                assertEquals(1, blockingQueue.size());
                assertEquals(1, simpSession.getSubscriptions().size());
                assertEquals(1, simpUserRegistry.getUserCount());
                ChessGameWebsocketMessage chessGameWebsocketMessage = blockingQueue.take();
                assertEquals(WebsocketMessageType.GAME_STATE, chessGameWebsocketMessage.getType());
                assertThat(jsonObjectBoardResponse)
                    .usingRecursiveComparison()
                    .withComparatorForFields(notNullComparator(), DYNAMIC_FIELDS)
                    .isEqualTo(chessGameWebsocketMessage.getJsonObjectBoardResponse());
            });
    }
    
    @Test
    void shouldReceiveMessageAboutPawnPromotion() throws Exception {
        givenWeHaveACustomBoardWithTwoUsersForUserOneMove(
            List.of(
                new ChessMoveRequest(1, 0, 2, 7),
                new ChessMoveRequest(0, 0, 3, 7),
                new ChessMoveRequest(6, 0, 1, 0)
            )
        );
        andWeHaveWsUrlWithAccessTokenForUser(getPlayerOneAccessToken());
        andWeHaveAStompClient();
        andWeSetBlockingQueueAndConverterType();
        andWeAsyncConnectToWebSocket();
        andWeSubscribeToChessBoardAndExpectAStringPayload();
        andWeFindSimpSessionForUser(applicationUser);
        andPlayerOnesMakesAMoveThatPutsPawnPromotionPendingAsTrue();
        whenUserOneMakesPawnPromotionRequest();

        await()
            .atMost(5, SECONDS)
            .untilAsserted(() -> {
                assertEquals(3, blockingQueue.size());
                assertEquals(1, simpSession.getSubscriptions().size());
                assertEquals(1, simpUserRegistry.getUserCount());
                ChessGameWebsocketMessage chessGameWebsocketMessage1 = blockingQueue.take();
                ChessGameWebsocketMessage chessGameWebsocketMessage2 = blockingQueue.take();
                ChessGameWebsocketMessage chessGameWebsocketMessage3 = blockingQueue.take();
                assertEquals(WebsocketMessageType.GAME_STATE, chessGameWebsocketMessage1.getType());
                assertEquals(WebsocketMessageType.NOTIFICATION, chessGameWebsocketMessage2.getType());
                assertEquals("Pawn promotion pending", chessGameWebsocketMessage2.getMessage());
                assertEquals(WebsocketMessageType.GAME_STATE, chessGameWebsocketMessage3.getType());
                assertThat(jsonObjectBoardResponse)
                    .usingRecursiveComparison()
                    .withComparatorForFields(notNullComparator(), DYNAMIC_FIELDS)
                    .isEqualTo(chessGameWebsocketMessage3.getJsonObjectBoardResponse());
            });
    }

    private void andPlayerOnesMakesAMoveThatPutsPawnPromotionPendingAsTrue() throws Exception {
        mvcResult = mockMvc.perform(post(MAKE_A_MOVE_PATH + savedBoardEntity.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerOneAccessToken())
                .content(getMoveJsonRequestBody(1, 0, 0, 0)))
            .andExpect(jsonPath("$.PawnPromotionPending").value(true))
            .andExpect(status().isCreated())
            .andReturn();
        jsonObjectBoardResponse = deserialize(mvcResult.getResponse().getContentAsString(), JsonObjectBoardResponse.class);
    }

    private void whenUserOneMakesPawnPromotionRequest() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("upgradedPieceName", "Queen");

        mvcResult = mockMvc.perform(post(PROMOTE_PAWN_PATH + savedBoardEntity.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerOneAccessToken())
                .content(OBJECT_MAPPER.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user2"))
            .andExpect(jsonPath("$.PawnPromotionPending").value("false"))
            .andReturn();
        jsonObjectBoardResponse = deserialize(mvcResult.getResponse().getContentAsString(), JsonObjectBoardResponse.class);
    }

    private void whenPlayerTwoJoinsGame() throws Exception {
        mockMvc.perform(post(ADD_PLAYER_TWO_TO_BOARD_PATH + savedBoardEntity.getId())
                .header("Authorization", "Bearer " + getPlayerTwoAccessToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andReturn();
    }

    private void whenPlayerOneMakeAMove() throws Exception {
        mvcResult = mockMvc.perform(post(MAKE_A_MOVE_PATH + savedBoardEntity.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getPlayerOneAccessToken())
                .content(getMoveJsonRequestBody(6, 3, 4, 3)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.Players.PlayerOneUsername").value("test_user1"))
            .andExpect(jsonPath("$.Players.PlayerTwoUsername").value("test_user2"))
            .andExpect(jsonPath("$.Players.ActivePlayerUsername").value("test_user2"))
            .andReturn();
        jsonObjectBoardResponse = deserialize(mvcResult.getResponse().getContentAsString(), JsonObjectBoardResponse.class);
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

    private String getPlayerTwoAccessToken() throws Exception {
        String username = "test_user2";
        String password = "admin";

        mvcResult = mockMvc.perform(post(LOGIN_PATH)
                .with(httpBasic(username, password)))
            .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response).get("accessToken").toString();
    }

    private void andWeHaveWsUrlWithAccessTokenForUser(String accessToken) {
        URL = "ws://localhost:" + port + "/ws?access_token=" + accessToken;
    }

    private void andWeFindSimpSessionForUser(ApplicationUser applicationUser) {
        SimpUser user = simpUserRegistry.getUser(applicationUser.getUsername());
        assert user != null;
        Set<SimpSession> sessions = user.getSessions();
        simpSession = sessions.stream().findFirst().orElseThrow();
    }

    private void whenWeSendMessageUsingStompSession() {
        stompSession.send("/topic/game-progress/" + savedBoardEntity.getId(), ChessGameWebsocketMessage.Builder.builder().message("Test Message").type(WebsocketMessageType.NOTIFICATION).build());
    }

    private void andWeSubscribeToChessBoardAndExpectAStringPayload() {
        stompSession.subscribe(SUBSCRIBE_BOARD_ENDPOINT + savedBoardEntity.getId(), new StompFrameHandler() {

            @Override
            @Nonnull
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return ChessGameWebsocketMessage.class;
            }

            @Override
            public void handleFrame(@Nonnull StompHeaders headers, Object payload) {
                blockingQueue.add((ChessGameWebsocketMessage) payload);
            }
        });
    }

    private void andWeAsyncConnectToWebSocket() throws InterruptedException, ExecutionException, TimeoutException {
        stompSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);
    }

    private void andWeSetBlockingQueueAndConverterType() {
        blockingQueue = new ArrayBlockingQueue<>(3);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    private void andWeHaveAStompClient() {
        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
    }

    private List<Transport> createTransportClient() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024);
        container.setDefaultMaxTextMessageBufferSize(1024 * 1024);
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient(container)));
        return transports;
    }

    private void createUserOne() {
        ApplicationUser testUser1 = new ApplicationUser("test-user-data-id1", "test_user1", "$2a$10$0gJXCjduBg9FgnvFm8E7I.VWOejHp7J/Xpa/DMLu9xENiOm61FUxS");
        applicationUser = applicationUserRepository.saveAndFlush(testUser1);
    }

    private void andWeHaveUserTwo() {
        ApplicationUser testUser2 = new ApplicationUser("test-user-data-id2", "test_user2", "$2a$10$0gJXCjduBg9FgnvFm8E7I.VWOejHp7J/Xpa/DMLu9xENiOm61FUxS");
        applicationUser2 = applicationUserRepository.saveAndFlush(testUser2);
    }

    private void givenWeHaveABoardWithOneUser() {
        createUserOne();

        Board board = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(board);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(applicationUser.getId());
        playersEntity.setPlayerOneAppUsername(applicationUser.getUsername());
        playersEntity.setActivePlayerUsername(applicationUser.getUsername());
        boardEntity.setPlayersEntity(playersEntity);
        savedBoardEntity = boardRepository.saveAndFlush(boardEntity);
    }

    private void givenWeHaveABoardWithTwoUsers() {
        createUserOne();
        andWeHaveUserTwo();

        Board board = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(board);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(applicationUser.getId());
        playersEntity.setPlayerTwoAppUserId(applicationUser2.getId());
        playersEntity.setPlayerOneAppUsername(applicationUser.getUsername());
        playersEntity.setPlayerTwoAppUsername(applicationUser2.getUsername());
        playersEntity.setActivePlayerUsername(applicationUser.getUsername());
        boardEntity.setPlayersEntity(playersEntity);
        savedBoardEntity = boardRepository.saveAndFlush(boardEntity);
    }

    private void givenWeHaveACustomBoardWithTwoUsersForUserOneMove(List<ChessMoveRequest> chessMoveRequestList) {
        createUserOne();
        andWeHaveUserTwo();

        Board board = new Board(chessMoveRequestList);
        board.setCurrentPlayerColourState("white");
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(board);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(applicationUser.getId());
        playersEntity.setPlayerTwoAppUserId(applicationUser2.getId());
        playersEntity.setPlayerOneAppUsername(applicationUser.getUsername());
        playersEntity.setPlayerTwoAppUsername(applicationUser2.getUsername());
        playersEntity.setActivePlayerUsername(applicationUser.getUsername());
        boardEntity.setPlayersEntity(playersEntity);
        savedBoardEntity = boardRepository.saveAndFlush(boardEntity);
    }

    private String getMoveJsonRequestBody(int currentRow, int currentColumn, int newRow, int newColumn) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("currentRow", currentRow);
        body.put("currentColumn", currentColumn);
        body.put("newRow", newRow);
        body.put("newColumn", newColumn);

        ObjectWriter ow = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(body);
    }

    private static <T> T deserialize(String response, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(response, clazz);
    }
}
