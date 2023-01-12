package com.lazychess.chessgame.chessGame;

import java.util.Arrays;

public class Queen extends Piece {

    public Queen(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void setLegalMoves(Square[][] squares) {
        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(square -> piecesInTheWayStraight(squares, square))
            .filter(square -> piecesInTheWayDiagonally(squares, square))
            .toList();
    }
}
