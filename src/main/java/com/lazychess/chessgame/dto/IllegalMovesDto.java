package com.lazychess.chessgame.dto;

import java.util.List;

import com.lazychess.chessgame.chessgame.LegalMoveSquare;

public record IllegalMovesDto(String pieceName, List<LegalMoveSquare> illegalMoves) {
}
