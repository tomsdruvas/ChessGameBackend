package com.lazychess.chessgame.chessGameTest;





import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessGame.Board;
import com.lazychess.chessgame.chessGame.Piece;
import com.lazychess.chessgame.chessGame.Square;

@SpringBootTest
class RookTest {

    @Test
    void loadChessBoardForRook() {
        Board board = new Board();
        Piece whiteRook2 = board.getSquares()[7][7].getPiece();
        List<Square> legalMoves = whiteRook2.getLegalMoves();
        assertThat(legalMoves).isEmpty();
    }

    @Test
    void loadChessBoardForRook2() {
        Board board = new Board();
        Piece whiteRook2 = board.getSquares()[7][7].getPiece();
        List<Square> legalMoves = whiteRook2.getLegalMoves();
        assertThat(legalMoves).isEmpty();
    }

    @Test
    void loadChessBoardForKing() {
        Board board = new Board();
        board.movePiece(7,6,5,5);
        Piece blackKing = board.getSquares()[0][4].getPiece();
        blackKing.setPieceRow(4);
        blackKing.setPieceColumn(7);
        board.getSquares()[4][7].setPiece(blackKing);
        board.getSquares()[0][4].clearPiece();
        board.movePiece(6,6,5,6);



    }
}
