package com.lazychess.chessgame.chessGame;


import static com.lazychess.chessgame.chessGame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessGame.ChessConstants.WHITE;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Square {

    private int row;
    private int column;
    private String colour;

    private Piece piece;

    public Square(int row, int column, boolean squareColor) {
        this.row = row;
        this.column = column;
        this.colour = squareColor ? (WHITE) : (BLACK);
        this.piece = new EmptyPiece();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
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
