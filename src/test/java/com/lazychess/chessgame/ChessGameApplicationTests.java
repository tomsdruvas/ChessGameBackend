package com.lazychess.chessgame;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessGame.Board;
import com.lazychess.chessgame.chessGame.Piece;
import com.lazychess.chessgame.chessGame.Square;

@SpringBootTest
class ChessGameApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void loadChessBoardForRook() {
		Board board = new Board();
		Piece whitePawn7 = board.getPieceByName("White Pawn8");
		board.movePiece(whitePawn7.getPieceRow(), whitePawn7.getPieceColumn(),5,6);
		Piece whiteRook2 = board.getPieceByName("White Rook2");
		whiteRook2.setLegalMoves(board.getSquares());
		List<Square> legalMoves = whiteRook2.getLegalMoves();


	}

	@Test
	void loadChessBoardForBishop() {
		Board board = new Board();
		Piece whitePawn7 = board.getPieceByName("White Pawn7");
		board.movePiece(whitePawn7.getPieceRow(), whitePawn7.getPieceColumn(),4,6);
		Piece whiteRook2 = board.getPieceByName("White Bishop2");
		board.movePiece(6,4,5,4);
		board.movePiece(1,5,3,5);
		List<Square> legalMoves = whiteRook2.getLegalMoves();
	}

	@Test
	void loadChessBoardForKnight() {
		Board board = new Board();
		Piece whiteRook2 = board.getPieceByName("White Knight2");
		whiteRook2.setLegalMoves(board.getSquares());
		List<Square> legalMoves = whiteRook2.getLegalMoves();
	}

	@Test
	void testMaths() {
		double pow = Math.pow(-1, 0);
		double pow1 = Math.pow(-1, 1);


	}
}
