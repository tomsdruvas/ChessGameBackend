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
	void loadChessBoard() {
		Board board = new Board();
		Piece whitePawn7 = board.getPieceByName("White Pawn8");
		board.movePiece(whitePawn7.getRow(), whitePawn7.getColumn(),5,6);
		Piece whiteRook2 = board.getPieceByName("White Rook2");
		whiteRook2.setLegalMoves(board.getSquares());
		List<Square> legalMoves = whiteRook2.getLegalMoves();


	}

}
