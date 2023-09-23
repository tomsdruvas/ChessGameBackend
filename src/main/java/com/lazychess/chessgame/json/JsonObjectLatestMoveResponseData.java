package com.lazychess.chessgame.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = JsonObjectLatestMoveResponseData.Builder.class)
@JsonInclude(NON_NULL)
public class JsonObjectLatestMoveResponseData {

    private static final String ROW = "Row";
    private static final String COLUMN = "Column";

    private final int row;
    private final int column;

    private JsonObjectLatestMoveResponseData(JsonObjectLatestMoveResponseData.Builder builder) {
        row = builder.row;
        column = builder.column;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @JsonProperty(ROW)
    public int getRow() {
        return row;
    }

    @JsonProperty(COLUMN)
    public int getColumn() {
        return column;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private int row;
        private int column;

        private Builder() {
        }

        @JsonProperty(ROW)
        public Builder row(int val) {
            row = val;
            return this;
        }

        @JsonProperty(COLUMN)
        public Builder column(int val) {
            column = val;
            return this;
        }

        public JsonObjectLatestMoveResponseData build() {
            return new JsonObjectLatestMoveResponseData(this);
        }
    }
}
