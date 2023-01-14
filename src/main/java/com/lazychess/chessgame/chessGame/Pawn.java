package com.lazychess.chessgame.chessGame;

import static com.lazychess.chessgame.chessGame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessGame.ChessConstants.EMPTY_PIECE;
import static com.lazychess.chessgame.chessGame.ChessConstants.WHITE;

import java.util.Arrays;
import java.util.List;
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
            .filter(this::twoFilterCombination)
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
        return square.getColumn() != getPieceColumn() && !Objects.equals(square.getPiece().getColour(), EMPTY_PIECE);
    }

    private boolean pawnCannotMoveTwoRowsAndOneColumn(Square square) {
        return !(Math.abs(getPieceRow() - square.getRow()) == 2 && Math.abs(getPieceColumn() - square.getColumn()) == 1);
    }

    private boolean pawnCannotTakePiecesOnAStraightLine(Square square) {
        return square.getColumn() == getPieceColumn() && Objects.equals(square.getPiece().getColour(), EMPTY_PIECE);
    }

    private boolean twoFilterCombination(Square square) {
        boolean a = pawnCannotTakePiecesOnAStraightLine(square);
        boolean b = pawnCanMoveDiagonallyToTakePiece(square);
        return a || b;
    }

    public List<Square> getStraightLegalMoves() {
        return legalMoves.stream().filter(square -> square.getColumn() == getPieceColumn()).toList();
    }

    public List<Square> getDiagonalLegalMovesToPreventTheKingFromGoingIntoCheckMate(Square[][] squares) {
        return Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::pawnCannotMoveMoreThanOneSquareAlongTheColumns)
            .filter(this::pawnCannotMoveMoreThanTwoSquareAlongTheRow)
            .filter(this::pawnCannotMoveTwoRowsAndOneColumn)
            .filter(this::pawnCannotMoveMoreThanOneSpaceAfterStartingPosition)
            .filter(this::pawnCannotMoveBack)
            .filter(this::getEmptyLegalDiagonalMoves)
            .toList();
    }

    private boolean getEmptyLegalDiagonalMoves(Square square) {
        return square.getColumn() != getPieceColumn() && square.getRow() != getPieceRow();
    }


// test better solution
//    public void generateLegalMoves(Square[][] squares) {
//        legalMoves = Arrays.stream(squares)
//            .flatMap(Arrays::stream)
//            .filter(this::isLegalPawnMove)
//            .toList();
//    }
//
//    private boolean isLegalPawnMove(Square square) {
//        int moveRow = Math.abs(getPieceRow() - square.getRow());
//        int moveCol = Math.abs(getPieceColumn() - square.getColumn());
//        boolean isMovingDiagonally = moveRow == 1 && moveCol == 1;
//        boolean isTakingPiece = square.getPiece().getColour() != EMPTY_PIECE && isMovingDiagonally;
//
//        if (isTakingPiece) {
//            return true;
//        }
//
//        if (moveCol > 0 || moveRow > 2) {
//            return false;
//        }
//
//        if (getPieceRow() == 1 && getColour() == BLACK) {
//            return moveRow <= 2;
//        } else if (getPieceRow() == 6 && getColour() == WHITE) {
//            return moveRow <= 2;
//        } else {
//            return moveRow == 1;
//        }
//    }
}
