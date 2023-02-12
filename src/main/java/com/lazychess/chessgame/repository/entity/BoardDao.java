package com.lazychess.chessgame.repository.entity;

import org.hibernate.annotations.Type;

import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.chessgame.Square;
import com.vladmihalcea.hibernate.type.array.StringArrayType;

import jakarta.persistence.Column;
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
public class BoardDao {

    @Id
    @Column(name = "cgb_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(StringArrayType.class)
    @Column(name = "cgb_squares", columnDefinition = "jsonb")
    private Square[][] squares;

    @Column(name = "cgb_game_state")
    @Enumerated(EnumType.STRING)
    private ChessGameState stateOfTheGame;

    @Column(name = "cgb_current_player_colour")
    private String currentPlayerColour;
}
