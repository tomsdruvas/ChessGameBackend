package com.lazychess.chessgame.chessGame;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Piece {

    String name;
    private String colour;
    private int row;
    private int column;
    private Board board;
    private List<Square> legalMoves;

    public Piece(String name, int row, int column, String colour) {
        this.name = name;
        this.row = row;
        this.column = column;
        this.colour = colour;
    }

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

    public void setLegalMoves(Square[][] legalMoves) {
    }

    public void move(Square square) {
        this.row = square.getRow();
        this.column = square.getColumn();
    }

    public String getName() {
        return name;
    }
}
