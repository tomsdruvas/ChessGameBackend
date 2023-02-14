package com.lazychess.chessgame.repository.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayersDao {

    @NotNull
    private String playerOneAppUserId;
    private String playerTwoAppUserId;
    @NotNull
    private String activePlayer;
}
