package com.lazychess.chessgame.chessGameTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lazychess.chessgame.chessGame.Board;
import com.lazychess.chessgame.chessGame.Piece;
import com.lazychess.chessgame.chessGame.Square;

class PawnTest {

    private static Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void allPawnsShouldHaveTwoLegalMovesAtTheStart() {
        for (int i = 0; i < 8; i++) {
            Piece whitePawn = board.getSquares()[6][i].getPiece();
            List<Square> legalMovesForWhitePawn = whitePawn.getLegalMoves();
            assertThat(legalMovesForWhitePawn).hasSize(2);

            assertThat(legalMovesForWhitePawn).allSatisfy(square -> {
                assertThat(square.getColumn()).isEqualTo(whitePawn.getPieceColumn());
                assertThat(square.getRow()).isNotEqualTo(whitePawn.getPieceRow());
            });
        }

        for (int i = 0; i < 8; i++) {
            Piece blackPawn = board.getSquares()[1][i].getPiece();
            List<Square> legalMovesForBlackPawn = blackPawn.getLegalMoves();
            assertThat(legalMovesForBlackPawn).hasSize(2);

            assertThat(legalMovesForBlackPawn).allSatisfy(square -> {
                assertThat(square.getColumn()).isEqualTo(blackPawn.getPieceColumn());
                assertThat(square.getRow()).isNotEqualTo(blackPawn.getPieceRow());
            });
        }
    }

    @Test
    void afterFirstMoveWhitePawnShouldOnlyBeAbleToMoveOneSpace() {
        board.movePiece(6,1,4,1);
        List<Square> legalMoves = board.getSquares()[4][1].getPiece().getLegalMoves();

        assertThat(legalMoves).hasSize(1);
        assertThat(legalMoves.get(0).getRow()).isEqualTo(3);
        assertThat(legalMoves.get(0).getColumn()).isEqualTo(1);
    }

    @Test
    void afterFirstMoveBlackPawnShouldOnlyBeAbleToMoveOneSpace() {
        board.movePiece(1,6,2,6);
        List<Square> legalMoves = board.getSquares()[2][6].getPiece().getLegalMoves();

        assertThat(legalMoves).hasSize(1);
        assertThat(legalMoves.get(0).getRow()).isEqualTo(3);
        assertThat(legalMoves.get(0).getColumn()).isEqualTo(6);
    }

    @Test
    void pawnShouldNotBeAbleToTakeAPieceStraight() {
        board.movePiece(6,1,4,1);
        board.movePiece(1,1,3,1);
        List<Square> legalMovesWhitePawn = board.getSquares()[4][1].getPiece().getLegalMoves();
        List<Square> legalMovesBlackPawn = board.getSquares()[3][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).isEmpty();
        assertThat(legalMovesBlackPawn).isEmpty();
    }

    @Test
    void pawnShouldBeAbleToTakeAPieceDiagonally() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);

        List<Square> legalMovesWhitePawn = board.getSquares()[4][2].getPiece().getLegalMoves();
        List<Square> legalMovesBlackPawn = board.getSquares()[3][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).hasSize(2);
        assertThat(legalMovesBlackPawn).hasSize(2);
    }

    @Test
    void pawnShouldNotBeAbleToTakeAPieceAlongTheColumn() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,2);

        List<Square> legalMovesWhitePawn = board.getSquares()[3][2].getPiece().getLegalMoves();
        List<Square> legalMovesBlackPawn = board.getSquares()[3][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).hasSize(1);
        assertThat(legalMovesBlackPawn).hasSize(1);
    }

    @Test
    void pawnShouldNotBeAbleToTakeAPieceDiagonallyBackwards() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,2);
        board.movePiece(3,1,4,1);

        List<Square> legalMovesWhitePawn = board.getSquares()[3][2].getPiece().getLegalMoves();
        List<Square> legalMovesBlackPawn = board.getSquares()[4][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).hasSize(1);
        assertThat(legalMovesBlackPawn).hasSize(1);
    }
}
