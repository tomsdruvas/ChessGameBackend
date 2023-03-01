package com.lazychess.chessgame.chessgame;

import java.util.Arrays;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Rook extends Piece {

    public Rook(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {

        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::rookCanMoveToSameColumnOrRow)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(square -> piecesInTheWayStraight(squares, square))
            .toList();

        setLegalMoves(legalMoves);
    }
}
