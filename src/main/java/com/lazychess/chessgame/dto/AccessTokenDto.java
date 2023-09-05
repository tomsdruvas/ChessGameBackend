package com.lazychess.chessgame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenDto(@JsonProperty("accessToken") String accessToken) {
}
