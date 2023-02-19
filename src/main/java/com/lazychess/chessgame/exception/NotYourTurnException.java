package com.lazychess.chessgame.exception;

public class NotYourTurnException extends RuntimeException {

    public NotYourTurnException(String message) {
        super(message);
    }
}
