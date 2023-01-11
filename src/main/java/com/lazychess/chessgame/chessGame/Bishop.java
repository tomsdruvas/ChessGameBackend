package com.lazychess.chessgame.chessGame;

import static com.lazychess.chessgame.chessGame.ChessConstants.EMPTY_PIECE;

import java.util.Arrays;
import java.util.Objects;

public class Bishop extends Piece {

    public Bishop(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void setLegalMoves(Square[][] squares) {
        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> !(square.getRow()==getRow() || square.getColumn() == getColumn()))
            .filter(square -> Math.abs(square.getRow() - getRow()) == Math.abs(square.getColumn() - getColumn()))
            .filter(square -> Objects.equals(square.getPiece().getColour(), EMPTY_PIECE) || !Objects.equals(square.getPiece().getColour(), getColour()))
            .filter(square -> {
                int currentRow = getRow();
                int currentColumn = getColumn();

                int newRow = square.getRow();
                int newColumn = square.getColumn();

                    int directionX = newRow > currentRow ? 1 : -1;
                    int directionY = newColumn > currentColumn ? 1 : -1;

                    for(int i = 1; i < Math.abs(newRow-currentRow); ++i) {
                        if(!Objects.equals(squares[currentRow + i * directionX][currentColumn + i * directionY].getPiece().getColour(), EMPTY_PIECE)) {
                            return false;
                        }
                    }
                    return true;
            })
            .toList();
    }
}
