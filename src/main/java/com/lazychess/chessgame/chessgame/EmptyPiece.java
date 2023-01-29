package com.lazychess.chessgame.chessgame;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyPiece extends Piece {

    @Override
    public String getColour() {
        return "empty";
    }
}