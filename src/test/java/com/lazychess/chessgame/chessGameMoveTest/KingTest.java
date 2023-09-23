package com.lazychess.chessgame.chessGameMoveTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveRequest;

class KingTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void bothKingsShouldHaveZeroLegalMovesWhenInitiated() {
        List<Piece> allKnights = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(this::findBothKingsByTheirStartingPosition)
            .toList();

        assertThat(allKnights).hasSize(2).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(King.class);
            assertThat(piece.getLegalMoves()).isEmpty();
        });
    }

    @Test
    void kingShouldHaveEightLegalMovesWhenItNoPiecesAreSurroundingIt() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 3, 4, 3)
        );

        Board board = new Board(preInitChessMoveRequestList);

        Piece whiteKing = board.getSquares()[4][3].getPiece();
        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves()).hasSize(8);
    }

    @Test
    void kingShouldBeAbleToTakeOppositePiece() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 3, 4, 3),
            new ChessMoveRequest(1, 3, 3, 3)

        );

        Board board = new Board(preInitChessMoveRequestList);

        Piece whiteKing = board.getSquares()[4][3].getPiece();
        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves()).hasSize(8)
                .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 3);

    }

    private boolean findBothKingsByTheirStartingPosition(Piece piece) {
        return
            (piece.getPieceRow() == 7 && piece.getPieceColumn() == 3) ||
                (piece.getPieceRow() == 0 && piece.getPieceColumn() == 3);
    }
}
