package com.lazychess.chessgame.exception;

public class WrongColourPieceOnSquareException extends RuntimeException {

    public WrongColourPieceOnSquareException(String message) {
        super(message);
    }
}
