package com.lazychess.chessgame.testUtil;

import com.lazychess.chessgame.chessgame.Piece;

import lombok.Builder;

@Builder
public class ChessMoveNotion {

    private String pieceColour;
    private Class<? extends Piece> pieceClass;
    private boolean shouldTakePiece;
    private boolean shouldEndInCheck;
    private int row;
    private int column;

//    public ChessMoveNotion(String pieceColour, TypeOfMoveEnum typeOfMove, boolean shouldEndInCheck, int row, int column) {
//        this.pieceColour = pieceColour;
//        this.typeOfMove = typeOfMove;
//        this.shouldEndInCheck = shouldEndInCheck;
//        this.row = row;
//        this.column = column;
//    }

    public boolean isShouldEndInCheck() {
        return shouldEndInCheck;
    }

    public void setShouldEndInCheck(boolean shouldEndInCheck) {
        this.shouldEndInCheck = shouldEndInCheck;
    }

    public String getPieceColour() {
        return pieceColour;
    }

    public void setPieceColour(String pieceColour) {
        this.pieceColour = pieceColour;
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
}
