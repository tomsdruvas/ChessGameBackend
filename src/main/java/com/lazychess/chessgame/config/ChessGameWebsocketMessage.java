package com.lazychess.chessgame.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;

@JsonDeserialize(builder = ChessGameWebsocketMessage.Builder.class)
@JsonInclude(NON_NULL)
public class ChessGameWebsocketMessage {

    private final WebsocketMessageType type;
    private final String message;
    private final JsonObjectBoardResponse jsonObjectBoardResponse;

    private ChessGameWebsocketMessage(Builder builder) {
        type = builder.type;
        message = builder.message;
        jsonObjectBoardResponse = builder.jsonObjectBoardResponse;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private WebsocketMessageType type;
        private String message;
        private JsonObjectBoardResponse jsonObjectBoardResponse;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder type(WebsocketMessageType val) {
            type = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public Builder jsonObjectBoardResponse(JsonObjectBoardResponse val) {
            jsonObjectBoardResponse = val;
            return this;
        }

        public ChessGameWebsocketMessage build() {
            return new ChessGameWebsocketMessage(this);
        }
    }

    public String getMessage() {
        return message;
    }

    public JsonObjectBoardResponse getJsonObjectBoardResponse() {
        return jsonObjectBoardResponse;
    }

    public WebsocketMessageType getType() {
        return type;
    }
}
