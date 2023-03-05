package com.lazychess.chessgame.chessgame;

import java.util.Arrays;
import java.util.List;

import com.lazychess.chessgame.config.CustomLegalSquareListMapper;

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
            .map(CustomLegalSquareListMapper::fromSquareToLegalMove)
            .toList();

        setLegalMoves(legalMoves);

    }

    private boolean knightCanMoveOneRowTwoColumnsOrOpposite(Square square) {
        return (Math.abs(getPieceRow() - square.getRow()) == 1 && Math.abs(getPieceColumn() - square.getColumn()) == 2) || (Math.abs(getPieceRow() - square.getRow()) == 2 && Math.abs(getPieceColumn() - square.getColumn()) == 1);
    }
}
