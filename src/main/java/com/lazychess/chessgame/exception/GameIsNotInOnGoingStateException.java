package com.lazychess.chessgame.exception;

public class GameIsNotInOnGoingStateException extends RuntimeException {

    public GameIsNotInOnGoingStateException(String message) {
        super(message);
    }
}
