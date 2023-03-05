package com.lazychess.chessgame.chessgame;

import java.io.Serializable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class LegalMoveSquare implements Serializable {

    private int row;
    private int column;
    private Piece piece;

    public LegalMoveSquare(int row, int column, Piece piece) {
        this.row = row;
        this.column = column;
        this.piece = piece;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Piece getPiece() {
        return piece;
    }
}
