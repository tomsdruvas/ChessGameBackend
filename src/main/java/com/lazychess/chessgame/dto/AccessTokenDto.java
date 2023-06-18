package com.lazychess.chessgame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenDto(@JsonProperty("access_token") String accessToken) {

}
