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
            .filter(this::queenLegalMoves)
            .filter(square -> checkForPiecesInWay(squares, square))

            .toList();
    }

    public boolean queenLegalMoves(Square square) {
        return bishopLegalMoves(square) || rookCanMoveToSameColumnOrRow(square);
    }

    public boolean checkForPiecesInWay(Square[][] squares, Square square) {

        boolean legalStraightMove = false;
        boolean legalDiagonalMove = false;

        if(bishopLegalMoves(square)) {
            legalStraightMove = piecesInTheWayDiagonally(squares, square);
        }

        if (rookCanMoveToSameColumnOrRow(square)) {
            legalDiagonalMove = piecesInTheWayStraight(squares, square);

        }

        return legalStraightMove || legalDiagonalMove;
    }
}