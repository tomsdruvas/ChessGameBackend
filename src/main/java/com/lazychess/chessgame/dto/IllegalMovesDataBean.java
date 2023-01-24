package com.lazychess.chessgame.dto;

import java.util.List;

import com.lazychess.chessgame.chessGame.Square;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IllegalMovesDataBean {

    private final String pieceName;
    private final List<Square> illegalMoves;

    public String getPieceName() {
        return pieceName;
    }

    public List<Square> getIllegalMoves() {
        return illegalMoves;
    }
}
