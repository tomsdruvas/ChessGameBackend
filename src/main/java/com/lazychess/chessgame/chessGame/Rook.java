package com.lazychess.chessgame.chessGame;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Rook extends Piece {

    private List<Square> legalMoves;

    public Rook(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void setLegalMoves(Square[][] squares) {

        this.legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> square.getColumn() == getColumn() || square.getRow() == getRow())
            .filter(square -> Objects.equals(square.getPiece().getColour(), "empty") || square.getPiece().getColour() != getColour())
            .filter(square -> {
                Piece piece;
                int currentColumn = getColumn();
                int currentRow = getRow();

                int newColumn = square.getColumn();
                int newRow = square.getRow();

                int direction;

                if(currentRow != newRow){
                    if(currentRow < newRow){
                        direction = 1;
                    }else{
                        direction = -1;
                    }

                    for(int x = currentRow + direction; x != newRow; x += direction){

                        if(squares[x][currentColumn].getPiece().getColour() != "empty") {

                            return false;
                        }
                    }
                }

                if(currentColumn != newColumn){
                    if(currentColumn < newColumn){
                        direction = 1;
                    }else{
                        direction = -1;
                    }

                    for(int x = currentColumn + direction; x != newColumn; x += direction){
                        if(squares[currentRow][x].getPiece().getColour() != "empty"){
                            return false;
                        }
                    }
                }
                return true;

            }).toList();

    }

    @Override
    public List<Square> getLegalMoves() {
        return legalMoves;
    }
}
