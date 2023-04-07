package com.lazychess.chessgame.chessgame;

public interface EnPassantAvailability {

    boolean enPassantAvailable();

    void setEnPassantAvailable();

    LegalMoveSquare enPassantMoveToAdd();

    void setEnPassantMoveToAdd(LegalMoveSquare legalMoveSquare);

    void setEnPassantPieceToRemove(LegalMoveSquare legalMoveSquare);

    LegalMoveSquare enPassantPieceToRemove();

    void clearEnPassant();
}
