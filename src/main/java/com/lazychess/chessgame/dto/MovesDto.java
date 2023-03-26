package com.lazychess.chessgame.dto;

import java.util.List;

import com.lazychess.chessgame.chessgame.LegalMoveSquare;

public record MovesDto(String pieceName, List<LegalMoveSquare> moves) {
}
