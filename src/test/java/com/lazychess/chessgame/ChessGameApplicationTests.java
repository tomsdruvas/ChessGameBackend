package com.lazychess.chessgame;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChessGameApplicationTests {


	@Test
	void contextLoads() {
	}

//	@Test
//	void loadChessBoardForRook() {
//		Board board = new Board();
//		List<Square> kingInDanger = isKingInDanger("white", board.getSquares());
//		List<Square> kingInDanger2 = isKingInDanger2("white", board.getSquares());
//
//	}
//
//	private List<Square> isKingInDanger2(String colour, Square[][] squares) {
//		return Arrays.stream(squares).flatMap(Arrays::stream)
//			.filter(square -> !Objects.equals(square.getPiece().getColour(), colour))
//			.map(square -> square.getPiece().getLegalMoves())
//			.filter(Objects::nonNull)
//			.filter(squares1 -> !squares1.isEmpty())
//			.flatMap(List::stream)
//			.filter(square -> (square.getPiece() instanceof King))
//			.toList();
//	}
//
//	private List<Square> isKingInDanger(String colour, Square[][] squares) {
//		return Arrays.stream(squares)
//			.flatMap(Arrays::stream)
//			.filter(square -> !square.getPiece().getColour().equals(colour))
//			.filter(square -> square.getPiece().getLegalMoves()!=null)
//			.flatMap(square -> square.getPiece().getLegalMoves().stream())
//			.filter(square -> square.getPiece() instanceof King)
//			.toList();
//	}
//
//	@Test
//	void loadChessBoardForBishop() {
//		Board board = new Board();
//		board.movePiece(6,7,4,6);
////		board.movePiece(6,2,5,2);
//	}
//
//	@Test
//	void loadChessBoardForKnight() {
//		Board board = new Board();
//		Piece whiteRook2 = board.getPieceByName("White Knight2");
//		whiteRook2.generateLegalMoves(board.getSquares());
//		List<Square> legalMoves = whiteRook2.getLegalMoves();
//	}
}
