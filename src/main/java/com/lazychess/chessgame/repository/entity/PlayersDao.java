package com.lazychess.chessgame.repository.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class PlayersDao {

    @NotNull
    private String playerOneAppUserId;
    @NotNull
    private String playerOneAppUsername;

    private String playerTwoAppUserId;
    private String playerTwoAppUsername;

    @NotNull
    private String activePlayerUsername;
}
