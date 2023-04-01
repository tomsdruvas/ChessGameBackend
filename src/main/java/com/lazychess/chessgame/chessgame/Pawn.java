package com.lazychess.chessgame.chessgame;

import static com.lazychess.chessgame.chessgame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessgame.ChessConstants.EMPTY_PIECE;
import static com.lazychess.chessgame.chessgame.ChessConstants.WHITE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;

import com.lazychess.chessgame.config.CustomLegalSquareListMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Pawn extends Piece implements EnPassenAvailability{

    private boolean enPassenAvailable = false;
    private LegalMoveSquare availableEnPassenMove;
    private LegalMoveSquare enPassenPieceToRemove;

    public Pawn(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        List<LegalMoveSquare> legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(this::pawnCannotMoveMoreThanOneSquareAlongTheColumns)
            .filter(this::pawnCannotMoveMoreThanTwoSquareAlongTheRow)
            .filter(this::pawnCannotMoveTwoRowsAndOneColumn)
            .filter(this::pawnCannotMoveMoreThanOneSpaceAfterStartingPosition)
            .filter(this::pawnCannotMoveBack)
            .filter(this::twoFilterCombination)
            .filter(square -> pawnCannotMoveTwoSquaresOnAStraightLineWhenBlockedByAnotherPiece(square, squares))
            .map(CustomLegalSquareListMapper::fromSquareToLegalMove)
            .toList();

        List<LegalMoveSquare> legalMoveSquaresWithEnPassenMoves = addEnPassenMovesIfItIsAvailable(legalMoves);

        setLegalMoves(legalMoveSquaresWithEnPassenMoves);
    }

    private List<LegalMoveSquare> addEnPassenMovesIfItIsAvailable(List<LegalMoveSquare> legalMoves) {
        if (enPassenAvailable) {
            List<LegalMoveSquare> enPassenMoveToAddList = Collections.singletonList(enPassenMoveToAdd());
            return ListUtils.union(legalMoves, enPassenMoveToAddList);
        }
        return legalMoves;
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

    public List<LegalMoveSquare> generateStraightLegalMoves() {
        if(legalMoves == null) {
            return List.of();
        }
        return legalMoves.stream().filter(square -> square.getColumn() == getPieceColumn()).toList();
    }

    public List<LegalMoveSquare> getDiagonalLegalMovesToPreventTheKingFromGoingIntoCheckMate(Square[][] squares) {
        return Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::pawnCannotMoveMoreThanOneSquareAlongTheColumns)
            .filter(this::pawnCannotMoveMoreThanTwoSquareAlongTheRow)
            .filter(this::pawnCannotMoveTwoRowsAndOneColumn)
            .filter(this::pawnCannotMoveMoreThanOneSpaceAfterStartingPosition)
            .filter(this::pawnCannotMoveBack)
            .filter(this::getEmptyLegalDiagonalMoves)
            .map(CustomLegalSquareListMapper::fromSquareToLegalMove)
            .toList();
    }

    private boolean getEmptyLegalDiagonalMoves(Square square) {
        return square.getColumn() != getPieceColumn() && square.getRow() != getPieceRow();
    }

    private boolean pawnCannotMoveTwoSquaresOnAStraightLineWhenBlockedByAnotherPiece(Square square, Square[][] squares) {
        if(getPieceRow() == 6 && Objects.equals(getColour(), "white")) {
            if(square.getColumn() == getPieceColumn()) {
                if(square.getRow() == 4) {
                    return squares[5][getPieceColumn()].getPiece().getClass() == EmptyPiece.class;
                }
            }
        } else if (getPieceRow() == 1 && Objects.equals(getColour(), "black")) {
            if(square.getColumn() == getPieceColumn()) {
                if(square.getRow() == 3) {
                    return squares[2][getPieceColumn()].getPiece().getClass() == EmptyPiece.class;
                }
            }
        }
        return true;
    }

    @Override
    public boolean enPassenAvailable() {
        return enPassenAvailable;
    }

    @Override
    public void setEnPassenAvailable() {
        this.enPassenAvailable = true;
    }

    @Override
    public LegalMoveSquare enPassenMoveToAdd() {
        return availableEnPassenMove;
    }

    @Override
    public void setEnPassenMoveToAdd(LegalMoveSquare legalMoveSquare) {
        this.availableEnPassenMove = legalMoveSquare;
    }

    @Override
    public void setEnPassenPieceToRemove(LegalMoveSquare legalMoveSquare) {
        this.enPassenPieceToRemove = legalMoveSquare;
    }

    @Override
    public LegalMoveSquare enPassenPieceToRemove() {
        return enPassenPieceToRemove;
    }

    @Override
    public void clearEnPassen() {
        this.enPassenAvailable = false;
        this.availableEnPassenMove = null;
    }
}
