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
            .filter(this::rookCanMoveToSameColumnOrRow)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(square -> piecesInTheWayStraight(squares, square))
            .toList();
    }

    private boolean rookCanMoveToSameColumnOrRow(Square square) {
        return square.getColumn() == getPieceColumn() || square.getRow() == getPieceRow();
    }
}
