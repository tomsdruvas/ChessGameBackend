package com.lazychess.chessgame.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthDetailsDto(@JsonProperty("accessToken") String accessToken, @JsonProperty("username") String username, @JsonProperty("roles") List<String> roles) {
}
