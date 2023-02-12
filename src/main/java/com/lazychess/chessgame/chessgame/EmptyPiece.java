package com.lazychess.chessgame.chessgame;

import java.util.List;

public class EmptyPiece extends Piece {

    public EmptyPiece() {
        setColour("empty");
        setLegalMoves(List.of());
    }

    @Override
    public String getColour() {
        return "empty";
    }
}