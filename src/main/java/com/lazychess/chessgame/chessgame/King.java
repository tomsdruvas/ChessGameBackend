package com.lazychess.chessgame.chessgame;

import java.util.Arrays;

public class King extends Piece {

    public King(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {

        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(this::kingLegalMoves)
            .toList();

        setLegalMoves(legalMoves);
    }

    private boolean kingLegalMoves(Square square) {

        boolean a = Math.abs(getPieceRow() - square.getRow()) == 1 && Math.abs(getPieceColumn() - square.getColumn()) == 1;
        boolean b = Math.abs(getPieceRow() - square.getRow()) == 1 && Math.abs(getPieceColumn() - square.getColumn()) == 0;
        boolean c = Math.abs(getPieceRow() - square.getRow()) == 0 && Math.abs(getPieceColumn() - square.getColumn()) == 1;

        return a || b || c;
    }
}