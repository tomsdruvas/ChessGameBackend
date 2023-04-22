package com.lazychess.chessgame.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = JsonObjectPlayersResponseData.Builder.class)
@JsonInclude(NON_NULL)
public class JsonObjectPlayersResponseData {

    private static final String PLAYER_ONE_ID_JSON_PROPERTY = "PlayerOneAppUserId";
    private static final String PLAYER_ONE_USERNAME_JSON_PROPERTY = "PlayerOneUsername";
    private static final String PLAYER_TWO_ID_JSON_PROPERTY = "PlayerTwoAppUserId";
    private static final String PLAYER_TWO_USERNAME_JSON_PROPERTY = "PlayerTwoUsername";
    private static final String ACTIVE_PLAYER_USERNAME_JSON_PROPERTY = "ActivePlayerUsername";

    private final String playerOneAppUserId;
    private final String playerOneUsername;
    private final String playerTwoAppUserId;
    private final String playerTwoUsername;
    private final String activePlayerUsername;

    private JsonObjectPlayersResponseData(JsonObjectPlayersResponseData.Builder builder) {
        playerOneAppUserId = builder.playerOneAppUserId;
        playerOneUsername = builder.playerOneUsername;
        playerTwoAppUserId = builder.playerTwoAppUserId;
        playerTwoUsername = builder.playerTwoUsername;
        activePlayerUsername = builder.activePlayerUsername;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @JsonProperty(PLAYER_ONE_ID_JSON_PROPERTY)
    public String getPlayerOneAppUserId() {
        return playerOneAppUserId;
    }

    @JsonProperty(PLAYER_ONE_USERNAME_JSON_PROPERTY)
    public String getPlayerOneUsername() {
        return playerOneUsername;
    }

    @JsonProperty(PLAYER_TWO_ID_JSON_PROPERTY)
    public String getPlayerTwoAppUserId() {
        return playerTwoAppUserId;
    }

    @JsonProperty(PLAYER_TWO_USERNAME_JSON_PROPERTY)
    public String getPlayerTwoUsername() {
        return playerTwoUsername;
    }

    @JsonProperty(ACTIVE_PLAYER_USERNAME_JSON_PROPERTY)
    public String getActivePlayerUsername() {
        return activePlayerUsername;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String playerOneAppUserId;
        private String playerOneUsername;
        private String playerTwoAppUserId;
        private String playerTwoUsername;
        private String activePlayerUsername;

        private Builder() {
        }

        @JsonProperty(PLAYER_ONE_ID_JSON_PROPERTY)
        public Builder playerOneAppUserId(String val) {
            playerOneAppUserId = val;
            return this;
        }

        @JsonProperty(PLAYER_ONE_USERNAME_JSON_PROPERTY)
        public Builder playerOneUsername(String val) {
            playerOneUsername = val;
            return this;
        }

        @JsonProperty(PLAYER_TWO_ID_JSON_PROPERTY)
        public Builder playerTwoAppUserId(String val) {
            playerTwoAppUserId = val;
            return this;
        }

        @JsonProperty(PLAYER_TWO_USERNAME_JSON_PROPERTY)
        public Builder playerTwoUsername(String val) {
            playerTwoUsername = val;
            return this;
        }

        @JsonProperty(ACTIVE_PLAYER_USERNAME_JSON_PROPERTY)
        public Builder activePlayerUsername(String val) {
            activePlayerUsername = val;
            return this;
        }

        public JsonObjectPlayersResponseData build() {
            return new JsonObjectPlayersResponseData(this);
        }
    }
}
