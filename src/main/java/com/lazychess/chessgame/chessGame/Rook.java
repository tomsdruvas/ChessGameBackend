package com.lazychess.chessgame.chessGame;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Rook extends Piece {

    private String name;
    private int row;
    private int column;
    private String colour;
    private List<Square> legalMoves;

    public Rook(String name, int row, int column, String colour) {
        this.name = name;
        this.row = row;
        this.column = column;
        this.colour = colour;
    }

    public void setLegalMoves(Square[][] squares) {
        legalMoves.clear();

        this.legalMoves = Arrays.asList(squares)
            .parallelStream()
            .flatMap(Arrays::stream)
                .filter(square -> square.getColumn() == column || square.getRow() == row)
                .filter(square -> !(square.getColumn() == column && square.getRow() == row))
            .collect(Collectors.toList());

    }
}
