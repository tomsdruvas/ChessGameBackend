package com.lazychess.chessgame.service;

import static com.lazychess.chessgame.controller.ControllerConstants.TOPIC_GAME_PROGRESS_PATH;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.lazychess.chessgame.config.ChessGameWebsocketMessage;
import com.lazychess.chessgame.config.WebsocketMessageType;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;

@Service
public class WebSocketMessagingService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketMessagingService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendPlayerTwoJoinsGameNotification(String chessGameId, String username) {
        ChessGameWebsocketMessage message = ChessGameWebsocketMessage.Builder.builder()
                .type(WebsocketMessageType.NOTIFICATION)
                .message(username + " has joined the game")
                .build();
        simpMessagingTemplate.convertAndSend(TOPIC_GAME_PROGRESS_PATH + chessGameId, message);
    }

    public void sendGameState(String chessGameId, JsonObjectBoardResponse jsonObjectBoardResponse) {
        ChessGameWebsocketMessage gameStateMessage = ChessGameWebsocketMessage.Builder.builder()
            .type(WebsocketMessageType.GAME_STATE)
            .jsonObjectBoardResponse(jsonObjectBoardResponse)
            .build();
        simpMessagingTemplate.convertAndSend(TOPIC_GAME_PROGRESS_PATH + chessGameId, gameStateMessage);

        handlePawnPromotionMessage(chessGameId, jsonObjectBoardResponse);
    }

    private void handlePawnPromotionMessage(String chessGameId, JsonObjectBoardResponse jsonObjectBoardResponse) {
        if (jsonObjectBoardResponse.getPawnPromotionPending()) {
            ChessGameWebsocketMessage message = ChessGameWebsocketMessage.Builder.builder()
                .type(WebsocketMessageType.NOTIFICATION)
                .message("Pawn promotion pending")
                .build();
            simpMessagingTemplate.convertAndSend(TOPIC_GAME_PROGRESS_PATH + chessGameId, message);
        }
    }
}
