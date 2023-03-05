package com.lazychess.chessgame.chessGameMoveITTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Piece;

@SpringBootTest
class ChessBoardTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }


    @Test
    void loadChessBoard() {
// Testing the white pawns
        for (int i = 0; i < 8; i++) {
            Piece whitePawn = board.getSquares()[6][i].getPiece();
            List<LegalMoveSquare> legalMovesForWhitePawn = whitePawn.getLegalMoves();
            assertThat(legalMovesForWhitePawn).hasSize(2);
        }

// Testing the black pawns
        for (int i = 0; i < 8; i++) {
            Piece blackPawn = board.getSquares()[1][i].getPiece();
            List<LegalMoveSquare> legalMovesForBlackPawn = blackPawn.getLegalMoves();
            assertThat(legalMovesForBlackPawn).hasSize(2);
        }

// Testing the white rooks
        Piece whiteRook1 = board.getSquares()[7][0].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteRook1 = whiteRook1.getLegalMoves();
        assertThat(legalMovesForWhiteRook1).isEmpty();

        Piece whiteRook2 = board.getSquares()[7][7].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteRook2 = whiteRook2.getLegalMoves();
        assertThat(legalMovesForWhiteRook2).isEmpty();

// Testing the black rooks
        Piece blackRook1 = board.getSquares()[0][0].getPiece();
        List<LegalMoveSquare> legalMovesForBlackRook1 = blackRook1.getLegalMoves();
        assertThat(legalMovesForBlackRook1).isEmpty();

        Piece blackRook2 = board.getSquares()[0][7].getPiece();
        List<LegalMoveSquare> legalMovesForBlackRook2 = blackRook2.getLegalMoves();
        assertThat(legalMovesForBlackRook2).isEmpty();

// Testing the white knights
        Piece whiteKnight1 = board.getSquares()[7][1].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteKnight1 = whiteKnight1.getLegalMoves();
        assertThat(legalMovesForWhiteKnight1).hasSize(2);

        Piece whiteKnight2 = board.getSquares()[7][6].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteKnight2 = whiteKnight2.getLegalMoves();
        assertThat(legalMovesForWhiteKnight2).hasSize(2);

// Testing the black knights
        Piece blackKnight1 = board.getSquares()[0][1].getPiece();
        List<LegalMoveSquare> legalMovesForBlackKnight1 = blackKnight1.getLegalMoves();
        assertThat(legalMovesForBlackKnight1).hasSize(2);

        Piece blackKnight2 = board.getSquares()[0][6].getPiece();
        List<LegalMoveSquare> legalMovesForBlackKnight2 = blackKnight2.getLegalMoves();
        assertThat(legalMovesForBlackKnight2).hasSize(2);

// Testing the white bishops
        Piece whiteBishop1 = board.getSquares()[7][2].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteBishop1 = whiteBishop1.getLegalMoves();
        assertThat(legalMovesForWhiteBishop1).isEmpty();

        Piece whiteBishop2 = board.getSquares()[7][5].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteBishop2 = whiteBishop2.getLegalMoves();
        assertThat(legalMovesForWhiteBishop2).isEmpty();

// Testing the black bishops
        Piece blackBishop1 = board.getSquares()[0][2].getPiece();
        List<LegalMoveSquare> legalMovesForBlackBishop1 = blackBishop1.getLegalMoves();
        assertThat(legalMovesForBlackBishop1).isEmpty();

        Piece blackBishop2 = board.getSquares()[0][5].getPiece();
        List<LegalMoveSquare> legalMovesForBlackBishop2 = blackBishop2.getLegalMoves();
        assertThat(legalMovesForBlackBishop2).isEmpty();

// Testing the white queen
        Piece whiteQueen = board.getSquares()[7][3].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteQueen = whiteQueen.getLegalMoves();
        assertThat(legalMovesForWhiteQueen).isEmpty();

// Testing the black queen
        Piece blackQueen = board.getSquares()[0][3].getPiece();
        List<LegalMoveSquare> legalMovesForBlackQueen = blackQueen.getLegalMoves();
        assertThat(legalMovesForBlackQueen).isEmpty();

// Testing the white king
        Piece whiteKing = board.getSquares()[7][4].getPiece();
        List<LegalMoveSquare> legalMovesForWhiteKing = whiteKing.getLegalMoves();
        assertThat(legalMovesForWhiteKing).isEmpty();

// Testing the black king
        Piece blackKing = board.getSquares()[0][4].getPiece();
        List<LegalMoveSquare> legalMovesForBlackKing = blackKing.getLegalMoves();
        assertThat(legalMovesForBlackKing).isEmpty();
    }

    @Test
    void exceptionThrownIfWrongColourTriesToPlay() {
        assertThatThrownBy(() -> board.movePiece(1,0,2,0))
            .hasMessage("It is not the black's turn");
    }
}
