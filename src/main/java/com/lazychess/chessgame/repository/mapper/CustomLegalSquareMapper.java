package com.lazychess.chessgame.repository.mapper;

import org.springframework.util.SerializationUtils;

import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Square;

public class CustomLegalSquareMapper {

    private CustomLegalSquareMapper() {
    }

    public static LegalMoveSquare fromSquareToLegalMove(Square square) {
        return new LegalMoveSquare(square.getRow(), square.getColumn(), removeLegalMovesField(square.getPiece()));
    }

    private static Piece removeLegalMovesField(Piece piece) {
        Piece clone = SerializationUtils.clone(piece);
        clone.clearLegalMoves();
        return clone;
    }
}