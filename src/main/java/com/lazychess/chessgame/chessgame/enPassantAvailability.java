package com.lazychess.chessgame.chessgame;

public interface enPassantAvailability {

    boolean enPassantAvailable();

    void setenPassantAvailable();

    LegalMoveSquare enPassantMoveToAdd();

    void setenPassantMoveToAdd(LegalMoveSquare legalMoveSquare);

    void setenPassantPieceToRemove(LegalMoveSquare legalMoveSquare);

    LegalMoveSquare enPassantPieceToRemove();

    void clearenPassant();
}
