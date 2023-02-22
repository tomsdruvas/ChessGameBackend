package com.lazychess.chessgame.chessgame;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChessConstants {

    public static final String WHITE = "white";
    public static final String BLACK = "black";
    public static final String EMPTY_PIECE = "empty";

    public static String oppositeColour(String colour) {
        if (Objects.equals(colour, WHITE)) {
            return BLACK;
        }
            return WHITE;

    }

}
