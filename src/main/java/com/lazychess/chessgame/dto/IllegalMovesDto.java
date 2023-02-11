package com.lazychess.chessgame.dto;

import java.util.List;

import com.lazychess.chessgame.chessgame.Square;

public record IllegalMovesDto(String pieceName, List<Square> illegalMoves) {
}
