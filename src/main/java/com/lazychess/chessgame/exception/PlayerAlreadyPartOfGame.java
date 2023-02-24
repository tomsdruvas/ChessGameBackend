package com.lazychess.chessgame.exception;

public class PlayerAlreadyPartOfGame extends RuntimeException {

    public PlayerAlreadyPartOfGame(String message) {
        super(message);
    }
}
