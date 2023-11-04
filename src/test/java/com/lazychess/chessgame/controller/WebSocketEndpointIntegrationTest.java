package com.lazychess.chessgame.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.lazychess.chessgame.config.TopicSubscriptionInterceptor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Disabled
public class WebSocketEndpointIntegrationTest {

    @MockBean
    private static TopicSubscriptionInterceptor topicSubscriptionInterceptor;

    @LocalServerPort
    private Integer port;

    private static final String SUBSCRIBE_BOARD_ENDPOINT = "/topic/game-progress/";
    private static String URL;

    @BeforeAll
    public static void setUp() {
    }

    @Test
    void shouldJoinChessgameWebsocket() throws ExecutionException, InterruptedException, TimeoutException {
//        when(topicSubscriptionInterceptor.preSend(any(), any())).thenReturn(new )
        URL = "ws://localhost:" + port + "/ws";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSession stompSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);
//        try {
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        stompSession.subscribe(SUBSCRIBE_BOARD_ENDPOINT + "mockChessGameId", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        stompSession.send("/app/welcome", "Mike");

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> assertEquals("Hello, Mike!", blockingQueue.poll()));


    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }
}
