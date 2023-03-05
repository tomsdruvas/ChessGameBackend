package com.lazychess.chessgame.chessgame;

import java.util.Arrays;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Queen extends Piece {

    public Queen(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        List<Square> legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(this::queenLegalMoves)
            .filter(square -> checkForPiecesInWay(squares, square))
            .toList();

        setLegalMoves(legalMoves);

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
