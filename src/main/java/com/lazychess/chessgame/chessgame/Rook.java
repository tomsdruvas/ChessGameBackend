package com.lazychess.chessgame.chessgame;

import java.util.Arrays;
import java.util.List;

import com.lazychess.chessgame.config.CustomLegalSquareListMapper;

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
            .map(CustomLegalSquareListMapper::fromSquareToLegalMove)
            .toList();

        setLegalMoves(legalMoves);
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void hasMoved() {
        this.hasMoved = true;
    }
}
