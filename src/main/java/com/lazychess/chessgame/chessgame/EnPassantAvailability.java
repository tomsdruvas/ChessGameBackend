package com.lazychess.chessgame.chessgame;

public interface EnPassantAvailability {

    boolean getEnPassantAvailable();

    void setEnPassantAvailable();

    LegalMoveSquare getEnPassantMoveToAdd();

    void setEnPassantMoveToAdd(LegalMoveSquare legalMoveSquare);

    void setEnPassantPieceToRemove(LegalMoveSquare legalMoveSquare);

    LegalMoveSquare getEnPassantPieceToRemove();

    void clearEnPassant();
}
