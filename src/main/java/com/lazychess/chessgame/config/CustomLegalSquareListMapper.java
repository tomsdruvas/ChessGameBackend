package com.lazychess.chessgame.config;

import java.util.List;

import org.springframework.util.SerializationUtils;

import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Square;

public class CustomLegalSquareListMapper {

    private CustomLegalSquareListMapper() {
    }

    public static List<LegalMoveSquare> fromSquaresList(List<Square> squares) {
        return squares.stream().map(square -> new LegalMoveSquare(square.getRow(), square.getColumn(), removeLegalMovesField(square.getPiece()))).toList();
    }

    private static Piece removeLegalMovesField(Piece piece) {
        Piece clone = SerializationUtils.clone(piece);
        clone.clearLegalMoves();
        return clone;
    }
}