package com.lazychess.chessgame.chessGameMoveTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Bishop;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Knight;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;

@SpringBootTest
class KnightTest {

    private static Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void allKnightsShouldHaveTwoLegalMovesWhenInitiated() {

        List<Piece> allKnights = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(this::findAllKnightsByTheirStartingPosition)
            .toList();

        assertThat(allKnights).hasSize(4).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(Knight.class);
            assertThat(piece.getLegalMoves()).hasSize(2);
        });
    }

    @Test
    void shouldHaveFourLegalMovesForKnight1() {
        board.movePiece(6,7,5,7);
        board.movePiece(0,1,2,2);
        List<LegalMoveSquare> legalMoves = board.getSquares()[2][2].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(5)
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 0)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 4)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 3);
    }

    @Test
    void shouldHaveFourLegalMovesForKnight2() {
        board.movePiece(7,6,5,5);
        List<LegalMoveSquare> legalMoves = board.getSquares()[5][5].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(5)
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 7)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 3)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 4);
    }

    @Test
    void shouldHaveFourLegalMovesForKnight3() {
        board.movePiece(6,7,5,7);
        board.movePiece(0,6,2,5);
        List<LegalMoveSquare> legalMoves = board.getSquares()[2][5].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(5)
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 7)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 3)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 4);
    }

    @Test
    void shouldHaveFourLegalMovesForKnight4() {
        board.movePiece(7,1,5,2);
        List<LegalMoveSquare> legalMoves = board.getSquares()[5][2].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(5)
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 0)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 4)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 3);
    }

    @Test
    void shouldHaveThreeLegalMovesForKnight1() {
        board.movePiece(6,7,5,7);
        board.movePiece(0,1,2,0);
        List<LegalMoveSquare> legalMoves = board.getSquares()[2][0].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(3)
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 2);

    }

    @Test
    void shouldHaveThreeLegalMovesForKnight2() {
        board.movePiece(6,7,5,7);
        board.movePiece(0,6,2,7);
        List<LegalMoveSquare> legalMoves = board.getSquares()[2][7].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(3)
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 5);
    }

    @Test
    void shouldHaveThreeLegalMovesForKnight3() {
        board.movePiece(7,1,5,0);
        List<LegalMoveSquare> legalMoves = board.getSquares()[5][0].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(3)
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 2);
    }

    @Test
    void shouldHaveThreeLegalMovesForKnight4() {
        board.movePiece(7,6,5,7);
        List<LegalMoveSquare> legalMoves = board.getSquares()[5][7].getPiece().getLegalMoves();
        assertThat(legalMoves).hasSize(3)
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 6)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 5);
    }

    @Test
    void knightCanTakeOppositePiece() {
        board.movePiece(6,7,5,7);
        board.movePiece(0,1,2,2);
        board.movePiece(6,3,4,3);
        board.movePiece(2,2,4,3);

        List<LegalMoveSquare> legalMoves = board.getSquares()[4][3].getPiece().getLegalMoves();
        List<Piece> allPieces = board.getAllPieces();
        assertThat(allPieces).hasSize(31);
        assertThat(legalMoves).hasSize(8)
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 2)
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 4)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 3 && square.getColumn() == 5)
            .anyMatch(square -> square.getRow() == 5 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 5 && square.getColumn() == 5)
            .anyMatch(square -> square.getRow() == 6 && square.getColumn() == 2)
            .anyMatch(square -> square.getRow() == 6 && square.getColumn() == 4);
    }

    @Test
    void knightCannotMoveToPutOwnKingInCheck() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 4, 2, 4),
            new ChessMoveDto(0, 6, 2, 5),
            new ChessMoveDto(7, 2, 4, 5)
        );

        Board board = new Board(preInitChessMoveDtoList);
        board.movePiece(4,5,3,6);

        Piece whiteBishop = board.getSquares()[3][6].getPiece();
        Piece blackKnight = board.getSquares()[2][5].getPiece();
        Piece blackKing= board.getSquares()[0][3].getPiece();

        assertThat(whiteBishop).isExactlyInstanceOf(Bishop.class);
        assertThat(blackKnight).isExactlyInstanceOf(Knight.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        assertThat(blackKnight.getLegalMoves()).isEmpty();
    }

    private boolean findAllKnightsByTheirStartingPosition(Piece piece) {
        return
            (piece.getPieceRow() == 7 && piece.getPieceColumn() == 1) ||
                (piece.getPieceRow() == 0 && piece.getPieceColumn() == 1) ||
                (piece.getPieceRow() == 0 && piece.getPieceColumn() == 6) ||
                (piece.getPieceRow() == 7 && piece.getPieceColumn() == 6);
    }
}
