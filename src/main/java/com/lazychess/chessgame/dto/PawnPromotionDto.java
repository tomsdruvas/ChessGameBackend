package com.lazychess.chessgame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PawnPromotionDto(@NotBlank @Pattern(regexp = "^Queen|^Rook|^Bishop|^Knight", message = "That is not a valid piece name") String upgradedPieceName) {
}
