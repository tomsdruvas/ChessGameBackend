package com.lazychess.chessgame.chessGame;

import static com.lazychess.chessgame.chessGame.ChessConstants.EMPTY_PIECE;

import java.util.Arrays;
import java.util.Objects;

public class Knight extends Piece {

    public Knight(String name, int row, int column, String colour) {
        super(name, row, column, colour);
    }

    @Override
    public void setLegalMoves(Square[][] squares) {
        legalMoves = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> Objects.equals(square.getPiece().getColour(), EMPTY_PIECE) || !Objects.equals(square.getPiece().getColour(), getColour()))
            .filter(square -> (Math.abs(getRow()-square.getRow()) == 1 && Math.abs(getColumn()-square.getColumn()) == 2) || (Math.abs(getRow()-square.getRow()) == 2 && Math.abs(getColumn()-square.getColumn()) == 1))
            .toList();
    }

}
