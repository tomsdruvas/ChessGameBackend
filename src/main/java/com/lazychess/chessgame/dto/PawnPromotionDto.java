package com.lazychess.chessgame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PawnPromotionDto(@NotBlank @Pattern(regexp = "^queen|^rook|^bishop|^knight") String upgradedPieceName) {
}
