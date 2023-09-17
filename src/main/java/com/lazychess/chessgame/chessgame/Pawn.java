package com.lazychess.chessgame.chessgame;

import static com.lazychess.chessgame.chessgame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessgame.ChessConstants.EMPTY_PIECE;
import static com.lazychess.chessgame.chessgame.ChessConstants.WHITE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;

import com.lazychess.chessgame.repository.mapper.CustomLegalSquareMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Pawn extends Piece implements EnPassantAvailability {

    private boolean enPassantAvailable;
    private LegalMoveSquare availableEnPassantMove;
    private LegalMoveSquare enPassantPieceToRemove;

    public Pawn(String name, int row, int column, String colour) {
        super(name, row, column, colour);
        this.enPassantAvailable = false;
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
            .map(CustomLegalSquareMapper::fromSquareToLegalMove)
            .toList();

        List<LegalMoveSquare> legalMoveSquaresWithEnPassantMoves = addEnPassantMovesIfItIsAvailable(legalMoves);

        setLegalMoves(legalMoveSquaresWithEnPassantMoves);
    }

    private List<LegalMoveSquare> addEnPassantMovesIfItIsAvailable(List<LegalMoveSquare> legalMoves) {
        if (enPassantAvailable) {
            List<LegalMoveSquare> enPassantMoveToAddList = Collections.singletonList(getEnPassantMoveToAdd());
            return ListUtils.union(legalMoves, enPassantMoveToAddList);
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
        return square.getColumn() != getPieceColumn() && !Objects.equals(square.getPiece().getColour(), EMPTY_PIECE) && square.getRow() != getPieceRow();
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
            .map(CustomLegalSquareMapper::fromSquareToLegalMove)
            .toList();
    }

    private boolean getEmptyLegalDiagonalMoves(Square square) {
        return square.getColumn() != getPieceColumn() && square.getRow() != getPieceRow();
    }

    private boolean pawnCannotMoveTwoSquaresOnAStraightLineWhenBlockedByAnotherPiece(Square square, Square[][] squares) {
        if(getPieceRow() == 6 && Objects.equals(getColour(), "white") && (square.getColumn() == getPieceColumn() && (square.getRow() == 4))) {
            return squares[5][getPieceColumn()].getPiece().getClass() == EmptyPiece.class;
        } else if (getPieceRow() == 1 && Objects.equals(getColour(), "black") && (square.getColumn() == getPieceColumn() && (square.getRow() == 3))) {
            return squares[2][getPieceColumn()].getPiece().getClass() == EmptyPiece.class;
        }
        return true;
    }

    @Override
    public boolean getEnPassantAvailable() {
        return enPassantAvailable;
    }

    @Override
    public void setEnPassantAvailable() {
        this.enPassantAvailable = true;
    }

    @Override
    public LegalMoveSquare getEnPassantMoveToAdd() {
        return availableEnPassantMove;
    }

    @Override
    public void setEnPassantMoveToAdd(LegalMoveSquare legalMoveSquare) {
        this.availableEnPassantMove = legalMoveSquare;
    }

    @Override
    public void setEnPassantPieceToRemove(LegalMoveSquare legalMoveSquare) {
        this.enPassantPieceToRemove = legalMoveSquare;
    }

    @Override
    public LegalMoveSquare getEnPassantPieceToRemove() {
        return enPassantPieceToRemove;
    }

    @Override
    public void clearEnPassant() {
        this.enPassantAvailable = false;
        this.availableEnPassantMove = null;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pawn pawn)) return false;
        return getPieceRow() == pawn.getPieceRow() &&
            getPieceColumn() == pawn.getPieceColumn() &&
            Objects.equals(getName(), pawn.getName()) &&
            Objects.equals(getColour(), pawn.getColour()) &&
            getEnPassantAvailable() == pawn.getEnPassantAvailable();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getPieceRow(), getPieceColumn(), getName(), getColour(), getEnPassantAvailable());
    }

    @Override
    public String toString() {
        return "Pawn{" +
            "name='" + getName() + '\'' +
            ", colour='" + getColour() + '\'' +
            ", row=" + getPieceRow() +
            ", column=" + getPieceColumn() +
            ", enPassantAvailable=" + getEnPassantAvailable() +
            '}';
    }
}
