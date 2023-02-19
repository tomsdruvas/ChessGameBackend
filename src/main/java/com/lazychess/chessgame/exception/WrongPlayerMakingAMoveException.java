package com.lazychess.chessgame.exception;

public class WrongPlayerMakingAMoveException extends RuntimeException {

    public WrongPlayerMakingAMoveException(String message) {
        super(message);
    }
}
