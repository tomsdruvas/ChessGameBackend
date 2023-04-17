package com.lazychess.chessgame.chessgame;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.lazychess.chessgame.repository.mapper.CustomLegalSquareMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Rook extends Piece implements CastlingHasMoved {

    private boolean hasMoved = false;

    public Rook(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        List<LegalMoveSquare> legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::rookCanMoveToSameColumnOrRow)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(square -> piecesInTheWayStraight(squares, square))
            .map(CustomLegalSquareMapper::fromSquareToLegalMove)
            .toList();

        setLegalMoves(legalMoves);
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void hasMoved() {
        this.hasMoved = true;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rook rook)) return false;
        return getPieceRow() == rook.getPieceRow() &&
            getPieceColumn() == rook.getPieceColumn() &&
            Objects.equals(getName(), rook.getName()) &&
            Objects.equals(getColour(), rook.getColour()) &&
            getHasMoved() == rook.getHasMoved();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getPieceRow(), getPieceColumn(), getName(), getColour(), getHasMoved());
    }

    @Override
    public String toString() {
        return "Rook{" +
            "name='" + getName() + '\'' +
            ", colour='" + getColour() + '\'' +
            ", row=" + getPieceRow() +
            ", column=" + getPieceColumn() +
            ", hasMoved=" + hasMoved +
            '}';
    }
}
