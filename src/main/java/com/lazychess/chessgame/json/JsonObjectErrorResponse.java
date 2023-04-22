package com.lazychess.chessgame.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = JsonObjectErrorResponse.Builder.class)
@JsonInclude(NON_NULL)
public class JsonObjectErrorResponse {

    private static final String MESSAGE_JSON_PROPERTY = "Message";

    private final String message;

    private JsonObjectErrorResponse(Builder builder) {
        message = builder.message;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @JsonProperty(MESSAGE_JSON_PROPERTY)
    public String getMessage() {
        return message;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String message;

        private Builder() {
        }

        @JsonProperty(MESSAGE_JSON_PROPERTY)
        public Builder message(String val) {
            message = val;
            return this;
        }

        public JsonObjectErrorResponse build() {
            return new JsonObjectErrorResponse(this);
        }
    }
}
