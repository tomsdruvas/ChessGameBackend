package com.lazychess.chessgame.chessgame;


import static com.lazychess.chessgame.chessgame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessgame.ChessConstants.WHITE;

import java.io.Serializable;
import java.util.Objects;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class Square implements Serializable {

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

    public boolean squareEmpty() {
        return getPiece().getClass() == EmptyPiece.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square square)) return false;
        return row == square.row && column == square.column && Objects.equals(colour, square.colour) && Objects.equals(piece, square.getPiece());
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, colour, piece);
    }

    @Override
    public String toString() {
        return "Square{" +
            "row=" + row +
            ", column=" + column +
            ", colour='" + colour + '\'' +
            ", piece=" + piece +
            '}';
    }
}
