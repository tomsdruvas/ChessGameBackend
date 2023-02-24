package com.lazychess.chessgame.exception;

public class GameHasFinishedException extends RuntimeException {

    public GameHasFinishedException(String message) {
        super(message);
    }
}
