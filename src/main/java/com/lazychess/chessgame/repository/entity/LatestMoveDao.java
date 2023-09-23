package com.lazychess.chessgame.repository.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class LatestMoveDao {

    @NotNull
    private int row;
    @NotNull
    private int column;

}
