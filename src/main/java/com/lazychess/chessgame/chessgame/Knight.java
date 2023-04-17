package com.lazychess.chessgame.chessgame;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.lazychess.chessgame.repository.mapper.CustomLegalSquareMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Knight extends Piece {

    public Knight(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        List<LegalMoveSquare> legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(this::knightCanMoveOneRowTwoColumnsOrOpposite)
            .map(CustomLegalSquareMapper::fromSquareToLegalMove)
            .toList();

        setLegalMoves(legalMoves);

    }

    private boolean knightCanMoveOneRowTwoColumnsOrOpposite(Square square) {
        return (Math.abs(getPieceRow() - square.getRow()) == 1 && Math.abs(getPieceColumn() - square.getColumn()) == 2) || (Math.abs(getPieceRow() - square.getRow()) == 2 && Math.abs(getPieceColumn() - square.getColumn()) == 1);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Knight knight)) return false;
        return getPieceRow() == knight.getPieceRow() &&
            getPieceColumn() == knight.getPieceColumn() &&
            Objects.equals(getName(), knight.getName()) &&
            Objects.equals(getColour(), knight.getColour());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getPieceRow(), getPieceColumn(), getName(), getColour());
    }

    @Override
    public String toString() {
        return "Knight{" +
            "name='" + getName() + '\'' +
            ", colour='" + getColour() + '\'' +
            ", row=" + getPieceRow() +
            ", column=" + getPieceColumn() +
            '}';
    }
}
