package com.lazychess.chessgame.repository.entity;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.config.SquareListConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chess_game_board")
@Getter
@Setter
public class BoardEntity {

    @Id
    @Column(name = "cgb_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Generated(GenerationTime.INSERT)
    private String id;

    @Column(name = "cgb_squares", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    @Convert(converter = SquareListConverter.class)
    private Square[][] squares;

    @Column(name = "cgb_game_state")
    @Enumerated(EnumType.STRING)
    private ChessGameState stateOfTheGame;

    @Column(name = "cgb_current_player_colour")
    private String currentPlayerColour;

    @Type(JsonBinaryType.class)
    @Column(name = "cgb_current_player_number", columnDefinition = "jsonb")
    private PlayersEntity playersEntity;

    @Column(name = "cgb_pawn_promotion_pending")
    @JsonInclude(Include.NON_NULL)
    private boolean pawnPromotionPending;

    @Type(JsonBinaryType.class)
    @Column(name = "cgb_latest_move", columnDefinition = "jsonb")
    private LatestMoveEntity latestMove;

    @Column(name = "cgb_winner_user_id")
    @JsonInclude(Include.NON_NULL)
    private String winnerUsername;
}
