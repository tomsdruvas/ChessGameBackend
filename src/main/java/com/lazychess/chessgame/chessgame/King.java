package com.lazychess.chessgame.chessgame;

import java.util.Arrays;
import java.util.List;

import com.lazychess.chessgame.config.CustomLegalSquareListMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class King extends Piece implements CastlingHasMoved {

    private boolean hasMoved = false;

    public King(String name, int row, int column, String colour) {
        super(name, row, column, colour);
        this.hasMoved = false;
    }

    @Override
    public void generateLegalMoves(Square[][] squares) {
        List<LegalMoveSquare> legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(this::filterSquaresWithSameColourPiece)
            .filter(this::kingLegalMoves)
            .map(CustomLegalSquareListMapper::fromSquareToLegalMove)
            .toList();

        setLegalMoves(legalMoves);
    }

    private boolean kingLegalMoves(Square square) {

        boolean a = Math.abs(getPieceRow() - square.getRow()) == 1 && Math.abs(getPieceColumn() - square.getColumn()) == 1;
        boolean b = Math.abs(getPieceRow() - square.getRow()) == 1 && Math.abs(getPieceColumn() - square.getColumn()) == 0;
        boolean c = Math.abs(getPieceRow() - square.getRow()) == 0 && Math.abs(getPieceColumn() - square.getColumn()) == 1;

        return a || b || c;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void hasMoved() {
        this.hasMoved = true;
    }
}
