package com.lazychess.chessgame.chessGameTest;

import static com.lazychess.chessgame.chessGame.ChessConstants.EMPTY_PIECE;
import static com.lazychess.chessgame.chessGame.ChessConstants.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    void afterFirstMoveWhitePawnShouldOnlyBeAbleToMoveOneSpaceForward() {
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

    @Test
    void pawnShouldBeAbleToTakeOppositeColourPieceDiagonally() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,1);


        Piece whitePawn = board.getSquares()[3][1].getPiece();

        assertThat(whitePawn.getColour()).isEqualTo(WHITE);
        assertThat(Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> !Objects.equals(piece.getColour(), EMPTY_PIECE))
            .toList())
            .hasSize(31);
    }

    @Test
    void pawnShouldNotBeAbleToMoveOnToItsOwnColour() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,1);
        board.movePiece(6,1,4,1);

        List<Square> legalMovesWhitePawn = board.getSquares()[4][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).isEmpty();
    }

    @Test
    void pawnShouldNotBeAbleToTakeBackwards() {
        board.movePiece(6,0,4,0);
        board.movePiece(1,1,3,1);
        board.movePiece(4,0,3,0);
        board.movePiece(1,7,2,7);
        board.movePiece(7,0,4,0);
        board.movePiece(3,1,4,0);

        List<Square> legalMovesWhitePawn = board.getSquares()[4][0].getPiece().getLegalMoves();


        assertThat(legalMovesWhitePawn).allSatisfy(square -> {
            assertThat(square.getColumn()).isZero();
            assertThat(square.getRow()).isEqualTo(5);

        }).hasSize(1);

        assertThat(Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> !Objects.equals(piece.getColour(), EMPTY_PIECE))
            .toList())
            .hasSize(31);
    }

    @Test
    void pawnCannotMakeAMoveThatPutsOwnKingInCheck() {
        board.movePiece(1,4,3,4);
        board.movePiece(6,0,4,0);
        board.movePiece(0,4,1,4);
        board.movePiece(7,0,5,0);
        board.movePiece(1,4,2,4);
        board.movePiece(5,0,5,1);
        board.movePiece(2,4,3,5);
        board.movePiece(5,1,3,1);
        List<Square> legalMoves = board.getSquares()[3][4].getPiece().getLegalMoves();
        assertThat(legalMoves).isEmpty();

        board.movePiece(1,6,3,6);
        board.movePiece(6,3,4,3);
        List<Square> legalMoves2 = board.getSquares()[3][4].getPiece().getLegalMoves();
        assertThat(legalMoves2).isEmpty();

        board.movePiece(3,1,4,1);
        List<Square> legalMoves3 = board.getSquares()[3][4].getPiece().getLegalMoves();
        assertThat(legalMoves3).hasSize(2);
    }
}
