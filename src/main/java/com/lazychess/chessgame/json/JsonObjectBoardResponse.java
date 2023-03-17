package com.lazychess.chessgame.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.lazychess.chessgame.chessgame.Square;

@JsonDeserialize(builder = JsonObjectBoardResponse.Builder.class)
@JsonInclude(NON_NULL)
public class JsonObjectBoardResponse {
    private static final String BOARD_ID_JSON_PROPERTY = "BoardId";
    private static final String SQUARES_JSON_PROPERTY = "Squares";
    private static final String GAME_STATE_JSON_PROPERTY = "GameState";
    private static final String CURRENT_PLAYER_COLOUR_JSON_PROPERTY = "CurrentPlayerColour";
    private static final String PLAYERS_JSON_PROPERTY = "Players";
    private static final String PAWN_PROMOTION_PENDING = "PawnPromotionPending";
    private static final String WINNER_JSON_PROPERTY = "WinnerUsername";

    private final String boardId;
    private final Square[][] squares;
    private final String gameState;
    private final String currentPlayerColour;
    private final JsonObjectPlayersResponseData players;
    private final boolean pawnPromotionPending;
    private final String winner;

    private JsonObjectBoardResponse(Builder builder) {
        boardId = builder.boardId;
        squares = builder.squares;
        gameState = builder.gameState;
        currentPlayerColour = builder.currentPlayerColour;
        players = builder.players;
        pawnPromotionPending = builder.pawnPromotionPending;
        winner = builder.winner;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(JsonObjectBoardResponse copy) {
        Builder builder = new Builder();
        builder.boardId = copy.getBoardId();
        builder.squares = copy.getSquares();
        builder.gameState = copy.getGameState();
        builder.currentPlayerColour = copy.getCurrentPlayerColour();
        builder.players = copy.getPlayers();
        builder.winner = copy.getWinner();

        return builder;
    }

    @JsonProperty(BOARD_ID_JSON_PROPERTY)
    public String getBoardId() {
        return boardId;
    }

    @JsonProperty(SQUARES_JSON_PROPERTY)
    public Square[][] getSquares() {
        return squares;
    }

    @JsonProperty(GAME_STATE_JSON_PROPERTY)
    public String getGameState() {
        return gameState;
    }

    @JsonProperty(CURRENT_PLAYER_COLOUR_JSON_PROPERTY)
    public String getCurrentPlayerColour() {
        return currentPlayerColour;
    }

    @JsonProperty(PLAYERS_JSON_PROPERTY)
    public JsonObjectPlayersResponseData getPlayers() {
        return players;
    }

    @JsonProperty(PAWN_PROMOTION_PENDING)
    public boolean getPawnPromotionPending() {
        return pawnPromotionPending;
    }

    @JsonProperty(WINNER_JSON_PROPERTY)
    public String getWinner() {
        return winner;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String boardId;
        private Square[][] squares;
        private String gameState;
        private String currentPlayerColour;
        private JsonObjectPlayersResponseData players;
        private boolean pawnPromotionPending;
        private String winner;

        private Builder() {
        }

        @JsonProperty(BOARD_ID_JSON_PROPERTY)
        public Builder boardId(String val) {
            boardId = val;
            return this;
        }

        @JsonProperty(SQUARES_JSON_PROPERTY)
        public Builder squares(Square[][] val) {
            squares = val;
            return this;
        }

        @JsonProperty(GAME_STATE_JSON_PROPERTY)
        public Builder gameState(String val) {
            gameState = val;
            return this;
        }

        @JsonProperty(CURRENT_PLAYER_COLOUR_JSON_PROPERTY)
        public Builder currentPlayerColour(String val) {
            currentPlayerColour = val;
            return this;
        }

        @JsonProperty(PLAYERS_JSON_PROPERTY)
        public Builder players(JsonObjectPlayersResponseData val) {
            players = val;
            return this;
        }

        @JsonProperty(PAWN_PROMOTION_PENDING)
        public Builder winner(boolean val) {
            pawnPromotionPending = val;
            return this;
        }

        @JsonProperty(WINNER_JSON_PROPERTY)
        public Builder winner(String val) {
            winner = val;
            return this;
        }

        public JsonObjectBoardResponse build() {
            return new JsonObjectBoardResponse(this);
        }
    }
}
