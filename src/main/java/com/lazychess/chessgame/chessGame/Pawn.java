package com.lazychess.chessgame.chessGame;

import static com.lazychess.chessgame.chessGame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessGame.ChessConstants.EMPTY_PIECE;
import static com.lazychess.chessgame.chessGame.ChessConstants.WHITE;

import java.util.Arrays;
import java.util.Objects;

public class Pawn extends Piece {

    public Pawn(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(this::pawnCannotMoveMoreThanOneSquareAlongTheColumns)
            .filter(this::pawnCannotMoveMoreThanTwoSquareAlongTheRow)
            .filter(this::pawnCannotMoveTwoRowsAndOneColumn)
            .filter(this::pawnCannotMoveMoreThanOneSpaceAfterStartingPosition)
            .filter(this::pawnCannotMoveBack)
            .filter(this::pawnCanMoveDiagonallyToTakePiece)
            .toList();
    }

    private boolean pawnCannotMoveMoreThanOneSquareAlongTheColumns(Square square) {
        return Math.abs(getPieceColumn() - square.getColumn()) <= 1;
    }

    private boolean pawnCannotMoveMoreThanTwoSquareAlongTheRow(Square square) {
        return Math.abs(getPieceRow() - square.getRow()) < 3;
    }

    private boolean pawnCannotMoveMoreThanOneSpaceAfterStartingPosition(Square square) {
        if (!((Objects.equals(getColour(), BLACK) && getPieceRow() == 1) || (Objects.equals(getColour(), WHITE) && getPieceRow() == 6))) {
            return Math.abs(square.getRow() - getPieceRow()) == 1;
        }
        return true;
    }

    private boolean pawnCannotMoveBack(Square square) {
        if (Objects.equals(getColour(), WHITE)) {
            return getPieceRow() >= square.getRow();
        }

        if (Objects.equals(getColour(), BLACK)) {
            return getPieceRow() <= square.getRow();
        }

        return true;
    }

    private boolean pawnCanMoveDiagonallyToTakePiece(Square square) {
        return square.getColumn() == getPieceColumn() || !Objects.equals(square.getPiece().getColour(), EMPTY_PIECE);
    }

    private boolean pawnCannotMoveTwoRowsAndOneColumn(Square square) {
        return !(Math.abs(getPieceRow() - square.getRow()) == 2 && Math.abs(getPieceColumn() - square.getColumn()) == 1);
    }
}
