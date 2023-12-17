package com.lazychess.chessgame.config;

import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.lazychess.chessgame.exception.PlayerNotPartOfGameException;
import com.lazychess.chessgame.repository.entity.BoardEntity;
import com.lazychess.chessgame.service.BoardService;

@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    private final BoardService boardService;

    public TopicSubscriptionInterceptor(BoardService boardService) {
        this.boardService = boardService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String user = Objects.requireNonNull(headerAccessor.getUser()).getName();
            String chessGameId = Objects.requireNonNull(message.getHeaders().get("simpDestination")).toString().replace("/topic/game-progress/", "");
            BoardEntity boardEntity = boardService.findChessGameById(chessGameId);

            try {
                boardService.checkIfPlayerIsPartOfThisGame(boardEntity, user);
            } catch (PlayerNotPartOfGameException e) {
                throw new MessagingException("User not part of this chess game");
            }
        }
        return message;
    }
}
