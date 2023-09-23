package com.lazychess.chessgame.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ChessMoveRequest(@Min(value = 0, message = "Number needs to be between 0 and 7")@Max(value = 7, message = "Number needs to be between 0 and 7")int currentRow,
                               @Min(value = 0, message = "Number needs to be between 0 and 7")@Max(value = 7, message = "Number needs to be between 0 and 7")int currentColumn,
                               @Min(value = 0, message = "Number needs to be between 0 and 7")@Max(value = 7, message = "Number needs to be between 0 and 7")int newRow,
                               @Min(value = 0, message = "Number needs to be between 0 and 7")@Max(value = 7, message = "Number needs to be between 0 and 7")int newColumn) {
}
