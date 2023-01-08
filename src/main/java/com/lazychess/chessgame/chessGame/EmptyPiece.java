package com.lazychess.chessgame.chessGame;

public class EmptyPiece extends Piece {

    private String colour = "empty";

    public EmptyPiece() {
    }

    @Override
    public String getColour() {
        return colour;
    }
}
