package com.lazychess.chessgame.exception;

public class PasswordsDontMatchException extends RuntimeException {

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
