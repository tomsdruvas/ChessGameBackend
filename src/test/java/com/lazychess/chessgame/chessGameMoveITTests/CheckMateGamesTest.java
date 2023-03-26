package com.lazychess.chessgame.chessGameMoveITTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;

@SpringBootTest
class CheckMateGamesTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void reverseFoolsMate_whiteWins() {
        board.movePiece(6,3,4,3);
        board.movePiece(1,2,2,2);
        board.movePiece(6,4,4,4);
        board.movePiece(1,1,3,1);
        board.movePiece(7,4,3,0);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }

    @Test
    void scholarsMate_whiteWins() {
        board.movePiece(6,3,4,3);
        board.movePiece(1,3,3,3);
        board.movePiece(7,2,4,5);
        board.movePiece(0,6,2,5);
        board.movePiece(7,4,3,0);
        board.movePiece(1,1,2,1);
        board.movePiece(3,0,5,2);
        board.movePiece(0,2,1,1);
        board.movePiece(5,2,1,2);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }

    @Test
    void caroKannDefenseSmotheredMate_whiteWins() {
        board.movePiece(6,3,4,3);
        board.movePiece(1,5,2,5);
        board.movePiece(6,4,4,4);
        board.movePiece(1,4,3,4);
        board.movePiece(7,6,5,5);
        board.movePiece(3,4,4,3);
        board.movePiece(5,5,4,3);
        board.movePiece(0,6,1,4);
        board.movePiece(7,4,6,3);
        board.movePiece(0,1,2,2);
        board.movePiece(4,3,2,4);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }

    @Disabled
    @Test
    void italianGameSmotheredMate_blackWins() {
        board.movePiece(6,3,4,3);

        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }

    @Test
    void testLotsOfLegalMoves() {
        board.movePiece(6,4,4,4);
        board.movePiece(1,4,3,4);
        board.movePiece(6,5,4,5);
        board.movePiece(1,5,2,5);
        board.movePiece(7,1,5,2);
        board.movePiece(0,1,2,2);
        board.movePiece(7,6,5,5);
        board.movePiece(1,3,2,3);
        board.movePiece(6,3,5,3);
        board.movePiece(0,6,1,4);
        board.movePiece(7,2,5,4);
        board.movePiece(3,4,4,5);
        board.movePiece(5,4,4,5);
        board.movePiece(1,6,3,6);
        board.movePiece(4,5,5,4);
        board.movePiece(0,2,2,4);
        board.movePiece(7,3,7,1);
        board.movePiece(0,3,0,1);
        board.movePiece(7,4,6,5);
        board.movePiece(0,5,1,6);
        board.movePiece(6,7,5,7);
        board.movePiece(0,7,0,5);
        board.movePiece(5,2,3,1);
        board.movePiece(2,5,3,5);
        board.movePiece(3,1,1,0);
        board.movePiece(2,2,4,1);
        board.movePiece(6,2,4,2);
        board.movePiece(3,5,4,4);
        board.movePiece(5,3,4,4);
        board.movePiece(2,4,3,5);
        board.movePiece(5,4,6,3);
        board.movePiece(1,4,3,3);
        board.movePiece(6,3,4,1);
        board.movePiece(3,5,4,4);
        board.movePiece(7,1,7,0);
        board.movePiece(3,3,4,1);
        board.movePiece(1,0,0,2);
        board.movePiece(1,2,3,2);
        board.movePiece(0,2,2,1);
        board.movePiece(0,4,2,2);
        board.movePiece(6,0,5,0);
        board.movePiece(2,2,2,1);
        board.movePiece(6,5,6,3);
        board.movePiece(2,1,3,0);
        board.movePiece(6,3,5,4);
        board.movePiece(4,4,5,3);
        board.movePiece(7,7,6,7);
        board.movePiece(1,6,6,1);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.ONGOING);
    }
}
