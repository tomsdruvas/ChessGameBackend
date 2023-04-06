package com.lazychess.chessgame.exception;

public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException(String boardId) {
        super("Board with ID: " + boardId + " does not exist");
    }
}
