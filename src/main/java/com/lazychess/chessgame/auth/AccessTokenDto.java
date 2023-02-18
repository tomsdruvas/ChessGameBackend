package com.lazychess.chessgame.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenDto(@JsonProperty("access_token") String accessToken) {

}
