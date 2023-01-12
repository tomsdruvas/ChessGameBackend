package com.lazychess.chessgame.chessGame;

import java.util.Arrays;

public class Knight extends Piece {

    public Knight(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void setLegalMoves(Square[][] squares) {
        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(this::knightCanMoveOneRowTwoColumnsOrOpposite)
                .toList();
    }

    private boolean knightCanMoveOneRowTwoColumnsOrOpposite(Square square) {
        return (Math.abs(getPieceRow() - square.getRow()) == 1 && Math.abs(getPieceColumn() - square.getColumn()) == 2) || (Math.abs(getPieceRow() - square.getRow()) == 2 && Math.abs(getPieceColumn() - square.getColumn()) == 1);
    }
}