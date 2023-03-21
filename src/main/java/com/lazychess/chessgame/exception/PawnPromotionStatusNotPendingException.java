package com.lazychess.chessgame.exception;

public class PawnPromotionStatusNotPendingException extends RuntimeException {

    public PawnPromotionStatusNotPendingException(String message) {
        super(message);
    }
}
