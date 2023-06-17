package com.lazychess.chessgame.chessGameMoveTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lazychess.chessgame.chessgame.Bishop;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Knight;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Rook;
import com.lazychess.chessgame.exception.EmptySourceSquareException;
import com.lazychess.chessgame.exception.IllegalMoveException;
import com.lazychess.chessgame.exception.WrongColourPieceOnSquareException;

class ChessBoardTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void assertWhitePawns() {
        for (int i = 0; i < 8; i++) {
            Piece whitePawn = board.getSquares()[6][i].getPiece();
            assertThat(whitePawn).isExactlyInstanceOf(Pawn.class);
            List<LegalMoveSquare> legalMovesForWhitePawn = whitePawn.getLegalMoves();
            assertThat(legalMovesForWhitePawn).hasSize(2);
            assertThat(whitePawn.getColour()).isEqualTo("white");

        }
    }

    @Test
    void assertBlackPawns() {
        for (int i = 0; i < 8; i++) {
            Piece blackPawn = board.getSquares()[1][i].getPiece();
            assertThat(blackPawn).isExactlyInstanceOf(Pawn.class);
            List<LegalMoveSquare> legalMovesForBlackPawn = blackPawn.getLegalMoves();
            assertThat(legalMovesForBlackPawn).hasSize(2);
            assertThat(blackPawn.getColour()).isEqualTo("black");
        }
    }

    @Test
    void assertWhiteRooks() {
        Piece whiteRook1 = board.getSquares()[7][0].getPiece();
        assertThat(whiteRook1).isExactlyInstanceOf(Rook.class);
        List<LegalMoveSquare> legalMovesForWhiteRook1 = whiteRook1.getLegalMoves();
        assertThat(legalMovesForWhiteRook1).isEmpty();
        assertThat(whiteRook1.getColour()).isEqualTo("white");

        Piece whiteRook2 = board.getSquares()[7][7].getPiece();
        assertThat(whiteRook2).isExactlyInstanceOf(Rook.class);
        List<LegalMoveSquare> legalMovesForWhiteRook2 = whiteRook2.getLegalMoves();
        assertThat(legalMovesForWhiteRook2).isEmpty();
        assertThat(whiteRook2.getColour()).isEqualTo("white");

    }

    @Test
    void assertBlackRooks() {
        Piece blackRook1 = board.getSquares()[0][0].getPiece();
        assertThat(blackRook1).isExactlyInstanceOf(Rook.class);
        List<LegalMoveSquare> legalMovesForBlackRook1 = blackRook1.getLegalMoves();
        assertThat(legalMovesForBlackRook1).isEmpty();
        assertThat(blackRook1.getColour()).isEqualTo("black");

        Piece blackRook2 = board.getSquares()[0][7].getPiece();
        assertThat(blackRook2).isExactlyInstanceOf(Rook.class);
        List<LegalMoveSquare> legalMovesForBlackRook2 = blackRook2.getLegalMoves();
        assertThat(legalMovesForBlackRook2).isEmpty();
        assertThat(blackRook2.getColour()).isEqualTo("black");
    }

    @Test
    void assertWhiteKnights() {
        Piece whiteKnight1 = board.getSquares()[7][1].getPiece();
        assertThat(whiteKnight1).isExactlyInstanceOf(Knight.class);
        List<LegalMoveSquare> legalMovesForWhiteKnight1 = whiteKnight1.getLegalMoves();
        assertThat(legalMovesForWhiteKnight1).hasSize(2);
        assertThat(whiteKnight1.getColour()).isEqualTo("white");

        Piece whiteKnight2 = board.getSquares()[7][6].getPiece();
        assertThat(whiteKnight2).isExactlyInstanceOf(Knight.class);
        List<LegalMoveSquare> legalMovesForWhiteKnight2 = whiteKnight2.getLegalMoves();
        assertThat(legalMovesForWhiteKnight2).hasSize(2);
        assertThat(whiteKnight2.getColour()).isEqualTo("white");
    }

    @Test
    void assertBlackKnights() {
        Piece blackKnight1 = board.getSquares()[0][1].getPiece();
        assertThat(blackKnight1).isExactlyInstanceOf(Knight.class);
        List<LegalMoveSquare> legalMovesForBlackKnight1 = blackKnight1.getLegalMoves();
        assertThat(legalMovesForBlackKnight1).hasSize(2);
        assertThat(blackKnight1.getColour()).isEqualTo("black");

        Piece blackKnight2 = board.getSquares()[0][6].getPiece();
        assertThat(blackKnight2).isExactlyInstanceOf(Knight.class);
        List<LegalMoveSquare> legalMovesForBlackKnight2 = blackKnight2.getLegalMoves();
        assertThat(legalMovesForBlackKnight2).hasSize(2);
        assertThat(blackKnight1.getColour()).isEqualTo("black");
    }

    @Test
    void assertWhiteBishops() {
        Piece whiteBishop1 = board.getSquares()[7][2].getPiece();
        assertThat(whiteBishop1).isExactlyInstanceOf(Bishop.class);
        List<LegalMoveSquare> legalMovesForWhiteBishop1 = whiteBishop1.getLegalMoves();
        assertThat(legalMovesForWhiteBishop1).isEmpty();
        assertThat(whiteBishop1.getColour()).isEqualTo("white");

        Piece whiteBishop2 = board.getSquares()[7][5].getPiece();
        assertThat(whiteBishop2).isExactlyInstanceOf(Bishop.class);
        List<LegalMoveSquare> legalMovesForWhiteBishop2 = whiteBishop2.getLegalMoves();
        assertThat(legalMovesForWhiteBishop2).isEmpty();
        assertThat(whiteBishop2.getColour()).isEqualTo("white");
    }

    @Test
    void assertBlackBishops() {
        Piece blackBishop1 = board.getSquares()[0][2].getPiece();
        assertThat(blackBishop1).isExactlyInstanceOf(Bishop.class);
        List<LegalMoveSquare> legalMovesForBlackBishop1 = blackBishop1.getLegalMoves();
        assertThat(legalMovesForBlackBishop1).isEmpty();
        assertThat(blackBishop1.getColour()).isEqualTo("black");

        Piece blackBishop2 = board.getSquares()[0][5].getPiece();
        assertThat(blackBishop2).isExactlyInstanceOf(Bishop.class);
        List<LegalMoveSquare> legalMovesForBlackBishop2 = blackBishop2.getLegalMoves();
        assertThat(legalMovesForBlackBishop2).isEmpty();
        assertThat(blackBishop2.getColour()).isEqualTo("black");
    }

    @Test
    void assertWhiteQueen() {
        Piece whiteQueen = board.getSquares()[7][4].getPiece();
        assertThat(whiteQueen).isExactlyInstanceOf(Queen.class);
        List<LegalMoveSquare> legalMovesForWhiteQueen = whiteQueen.getLegalMoves();
        assertThat(legalMovesForWhiteQueen).isEmpty();
        assertThat(whiteQueen.getColour()).isEqualTo("white");
    }

    @Test
    void assertBlackQueen() {
        Piece blackQueen = board.getSquares()[0][4].getPiece();
        assertThat(blackQueen).isExactlyInstanceOf(Queen.class);
        List<LegalMoveSquare> legalMovesForBlackQueen = blackQueen.getLegalMoves();
        assertThat(legalMovesForBlackQueen).isEmpty();
        assertThat(blackQueen.getColour()).isEqualTo("black");
    }

    @Test
    void assertWhiteKing() {
        Piece whiteKing = board.getSquares()[7][3].getPiece();
        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        List<LegalMoveSquare> legalMovesForWhiteKing = whiteKing.getLegalMoves();
        assertThat(legalMovesForWhiteKing).isEmpty();
        assertThat(whiteKing.getColour()).isEqualTo("white");
    }

    @Test
    void assertBlackKing() {
        Piece blackKing = board.getSquares()[0][3].getPiece();
        assertThat(blackKing).isExactlyInstanceOf(King.class);
        List<LegalMoveSquare> legalMovesForBlackKing = blackKing.getLegalMoves();
        assertThat(legalMovesForBlackKing).isEmpty();
        assertThat(blackKing.getColour()).isEqualTo("black");
    }

    @Test
    void exceptionThrownIfWrongColourTriesToPlay() {
        assertThatThrownBy(() -> board.movePiece(1,0,2,0)).isInstanceOf(WrongColourPieceOnSquareException.class)
            .hasMessage("Source square does not have your colour piece on it");
    }

    @Test
    void exceptionThrownIfMoveIsNotALegalMove() {
        assertThatThrownBy(() -> board.movePiece(6,0,5,1)).isInstanceOf(IllegalMoveException.class)
            .hasMessage("That is not a legal move for a Pawn");
    }

    @Test
    void exceptionThrownIfSourceSquareIsEmpty() {
        assertThatThrownBy(() -> board.movePiece(5,0,4,0)).isInstanceOf(EmptySourceSquareException.class)
            .hasMessage("Source square does not have a piece on it");
    }


}
