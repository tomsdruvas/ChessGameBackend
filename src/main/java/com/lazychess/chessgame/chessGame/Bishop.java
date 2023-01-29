package com.lazychess.chessgame.chessGame;

import static java.util.Arrays.stream;

import java.util.Arrays;

public class Bishop extends Piece {

    public Bishop(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        legalMoves = stream(squares)
            .flatMap(Arrays::stream)
            .filter(this::rowOrColumnCannotBeTheSame)
            .filter(this::bishopLegalMoves)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(square -> piecesInTheWayDiagonally(squares, square))
            .toList();

        setLegalMoves(legalMoves);
    }

    private boolean rowOrColumnCannotBeTheSame(Square square) {
        return !(square.getRow() == getPieceRow() || square.getColumn() == getPieceColumn());
    }
}
