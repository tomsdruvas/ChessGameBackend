package com.lazychess.chessgame.chessGame;

import java.util.ArrayList;

public class Queen extends Piece {

    private String name;
    private int row;
    private int column;
    private String colour;
    private ArrayList<Square> legalMoves;

    public Queen(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }
}
