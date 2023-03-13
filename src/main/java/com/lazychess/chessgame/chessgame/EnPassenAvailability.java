package com.lazychess.chessgame.chessgame;

public interface EnPassenAvailability {

    boolean enPassenAvailable();

    void setEnPassenAvailable();

    LegalMoveSquare enPassenMoveToAdd();

    void setEnPassenMoveToAdd(LegalMoveSquare legalMoveSquare);

    void setEnPassenPieceToRemove(LegalMoveSquare legalMoveSquare);

    LegalMoveSquare enPassenPieceToRemove();

    void clearEnPassen();
}
