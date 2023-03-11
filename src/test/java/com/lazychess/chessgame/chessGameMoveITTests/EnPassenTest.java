package com.lazychess.chessgame.chessGameMoveITTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.Piece;

@SpringBootTest
class EnPassenTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void kingPieceShouldHaveHasMovedAsTrueIfItHasMoved() {
        board.movePiece(6, 2, 4, 2);

        board.movePiece(1, 6, 2, 6);

        board.movePiece(4, 2, 3, 2);

        board.movePiece(1, 1, 3, 1);

        Piece piece = board.getSquares()[3][2].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 1);

    }
}
