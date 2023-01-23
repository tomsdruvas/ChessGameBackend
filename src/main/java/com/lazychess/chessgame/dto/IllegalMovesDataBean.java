package com.lazychess.chessgame.dto;

import java.util.List;

import com.lazychess.chessgame.chessGame.Square;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IllegalMovesDataBean {

    private String pieceName;
    private List<Square> illegalMoves;

    public String getPieceName() {
        return pieceName;
    }

    public void setPieceName(String pieceName) {
        this.pieceName = pieceName;
    }

    public List<Square> getIllegalMoves() {
        return illegalMoves;
    }

    public void setIllegalMoves(List<Square> illegalMoves) {
        this.illegalMoves = illegalMoves;
    }
}
