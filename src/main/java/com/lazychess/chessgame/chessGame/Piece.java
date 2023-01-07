package com.lazychess.chessgame.chessGame;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Piece {

    private String colour;
    private int row;
    private int column;
    private Board board;
    private List<Square> legalMoves;

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public List<Square> getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves(List<Square> legalMoves) {
        this.legalMoves = legalMoves;
    }

    public void move(Square square) {
        this.row = square.getRow();
        this.column = square.getColumn();
    }

}
