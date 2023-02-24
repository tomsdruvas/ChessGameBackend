package com.lazychess.chessgame.exception;

public class PlayerAlreadyPartOfGameException extends RuntimeException {

    public PlayerAlreadyPartOfGameException(String message) {
        super(message);
    }
}
