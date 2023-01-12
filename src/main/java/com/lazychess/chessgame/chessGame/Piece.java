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
    public List<Square> legalMoves;

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

    public int getPieceRow() {
        return row;
    }

    public void setPieceRow(int row) {
        this.row = row;
    }

    public int getPieceColumn() {
        return column;
    }

    public void setPieceColumn(int column) {
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
