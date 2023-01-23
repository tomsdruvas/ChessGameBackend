package com.lazychess.chessgame.chessGame;


import static com.lazychess.chessgame.chessGame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessGame.ChessConstants.WHITE;

import java.io.Serializable;
import java.util.Objects;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Square implements Serializable {

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

    public void clearPiece() {
        this.piece = new EmptyPiece();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square square)) return false;
        return row == square.row && column == square.column && Objects.equals(colour, square.colour);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
