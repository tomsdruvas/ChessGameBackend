package com.lazychess.chessgame.chessGame;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Piece {

    private String colour;
    private int x;
    private int y;
    private Board board;
    private ArrayList<Square> legalMoves;

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Square> getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves(ArrayList<Square> legalMoves) {
        this.legalMoves = legalMoves;
    }

    public void move(Square square) {
        this.x = square.getX();
        this.y = square.getY();
    }

}
