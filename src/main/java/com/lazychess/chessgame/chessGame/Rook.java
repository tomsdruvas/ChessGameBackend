package com.lazychess.chessgame.chessGame;

import static com.lazychess.chessgame.chessGame.ChessConstants.EMPTY_PIECE;

import java.util.Arrays;
import java.util.Objects;

public class Rook extends Piece {

    public Rook(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void setLegalMoves(Square[][] squares) {

        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> square.getColumn() == getPieceColumn() || square.getRow() == getPieceRow())
            .filter(square -> Objects.equals(square.getPiece().getColour(), EMPTY_PIECE) || !Objects.equals(square.getPiece().getColour(), getColour()))
            .filter(square -> {
                int currentColumn = getPieceColumn();
                int currentRow = getPieceRow();

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
                        if(!Objects.equals(squares[x][currentColumn].getPiece().getColour(), EMPTY_PIECE)) {

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
                        if(!Objects.equals(squares[currentRow][x].getPiece().getColour(), EMPTY_PIECE)){
                            return false;
                        }
                    }
                }
                return true;

            }).toList();
    }
}
