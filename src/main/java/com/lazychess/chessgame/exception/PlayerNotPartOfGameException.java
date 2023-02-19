package com.lazychess.chessgame.exception;

public class PlayerNotPartOfGameException extends RuntimeException {

    public PlayerNotPartOfGameException(String message) {
        super(message);
    }
}
