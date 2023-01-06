package com.lazychess.chessgame.chessGame;


import static com.lazychess.chessgame.chessGame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessGame.ChessConstants.WHITE;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Square {

    private int x;
    private int y;
    private String colour;


    private Piece piece;

    public Square(int x, int y, boolean squareColor) {
        this.x = x;
        this.y = y;
        this.colour = squareColor ? (WHITE) : (BLACK);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColour() {
        return colour;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
