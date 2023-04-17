package com.lazychess.chessgame.chessgame;

import java.util.List;
import java.util.Objects;

public class EmptyPiece extends Piece {

    public EmptyPiece() {
        setColour("empty");
        setLegalMoves(List.of());
    }

    @Override
    public String getColour() {
        return "empty";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmptyPiece emptyPiece)) return false;
        return getPieceRow() == emptyPiece.getPieceRow() &&
            getPieceColumn() == emptyPiece.getPieceColumn() &&
            Objects.equals(getName(), emptyPiece.getName()) &&
            Objects.equals(getColour(), emptyPiece.getColour());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getPieceRow(), getPieceColumn(), getName(), getColour());
    }

    @Override
    public String toString() {
        return "EmptyPiece{" +
            "name='" + getName() + '\'' +
            ", colour='" + getColour() + '\'' +
            ", row=" + getPieceRow() +
            ", column=" + getPieceColumn() +
            '}';
    }
}