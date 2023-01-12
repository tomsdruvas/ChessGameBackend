package com.lazychess.chessgame.chessGame;

import java.util.Arrays;

public class Bishop extends Piece {

    public Bishop(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void setLegalMoves(Square[][] squares) {
        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::rowOrColumnCannotBeTheSame)
            .filter(this::bishopLegalMoves)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(square -> piecesInTheWayDiagonally(squares, square))
            .toList();
    }

    private boolean rowOrColumnCannotBeTheSame(Square square) {
        return !(square.getRow() == getPieceRow() || square.getColumn() == getPieceColumn());
    }
}
