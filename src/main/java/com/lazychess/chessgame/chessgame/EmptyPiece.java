package com.lazychess.chessgame.chessgame;

public class EmptyPiece extends Piece {

    private String colour = "empty";

    public EmptyPiece() {
    }

    @Override
    public String getColour() {
        return colour;
    }
}
