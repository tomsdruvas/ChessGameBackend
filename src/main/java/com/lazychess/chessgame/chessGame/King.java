package com.lazychess.chessgame.chessGame;

import java.util.ArrayList;

public class King extends Piece {

    private String name;
    private int row;
    private int column;
    private String colour;
    private ArrayList<Square> legalMoves;

    public King(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }
}
