package com.lazychess.chessgame.chessgame;

import static java.util.Arrays.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.lazychess.chessgame.repository.mapper.CustomLegalSquareMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Bishop extends Piece {

    public Bishop(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        List<LegalMoveSquare> legalMoves = stream(squares)
            .flatMap(Arrays::stream)
            .filter(this::rowOrColumnCannotBeTheSame)
            .filter(this::bishopLegalMoves)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(square -> piecesInTheWayDiagonally(squares, square))
            .map(CustomLegalSquareMapper::fromSquareToLegalMove)
            .toList();

        setLegalMoves(legalMoves);
    }

    private boolean rowOrColumnCannotBeTheSame(Square square) {
        return !(square.getRow() == getPieceRow() || square.getColumn() == getPieceColumn());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bishop bishop)) return false;
        return getPieceRow() == bishop.getPieceRow() &&
            getPieceColumn() == bishop.getPieceColumn() &&
            Objects.equals(getName(), bishop.getName()) &&
            Objects.equals(getColour(), bishop.getColour());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getPieceRow(), getPieceColumn(), getName(), getColour());
    }

    @Override
    public String toString() {
        return "Bishop{" +
            "name='" + getName() + '\'' +
            ", colour='" + getColour() + '\'' +
            ", row=" + getPieceRow() +
            ", column=" + getPieceColumn() +
            '}';
    }
}
