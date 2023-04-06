package com.lazychess.chessgame.chessGameMoveTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.EmptyPiece;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.dto.ChessMoveDto;

@SpringBootTest
class EnPassenTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void enPassenMoveShouldBeAvailable_white() {
        board.movePiece(6, 2, 4, 2);

        board.movePiece(1, 6, 2, 6);

        board.movePiece(4, 2, 3, 2);

        board.movePiece(1, 1, 3, 1);

        Piece piece = board.getSquares()[3][2].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 1);
    }

    @Test
    void enPassenMoveShouldBeAvailable_black() {
        board.movePiece(6, 6, 5, 6);

        board.movePiece(1, 2, 3, 2);

        board.movePiece(6, 7, 5, 7);

        board.movePiece(3, 2, 4, 2);

        board.movePiece(6, 3, 4, 3);

        Piece piece = board.getSquares()[4][2].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 5 && square.getColumn() == 3);
    }

    @Test
    void enPassenMoveShouldBeAvailable_ThenDisappearInNextRound_white() {
        board.movePiece(6, 2, 4, 2);

        board.movePiece(1, 6, 2, 6);

        board.movePiece(4, 2, 3, 2);

        board.movePiece(1, 1, 3, 1);

        Piece piece = board.getSquares()[3][2].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 1);

        board.movePiece(6, 7, 5, 7);
        board.movePiece(1, 7, 2, 7);

        Piece piece2 = board.getSquares()[3][2].getPiece();

        assertThat(piece2.getLegalMoves())
            .noneMatch(square -> square.getRow() == 2 && square.getColumn() == 1);
    }

    @Test
    void enPassenMoveShouldBeAvailable_ThenDisappearInNextRound_black() {
        board.movePiece(6, 6, 5, 6);

        board.movePiece(1, 2, 3, 2);

        board.movePiece(6, 7, 5, 7);

        board.movePiece(3, 2, 4, 2);

        board.movePiece(6, 3, 4, 3);

        Piece piece = board.getSquares()[4][2].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 5 && square.getColumn() == 3);

        board.movePiece(1, 7, 2, 7);
        board.movePiece(6, 0, 5, 0);

        Piece piece2 = board.getSquares()[4][2].getPiece();

        assertThat(piece2.getLegalMoves())
            .noneMatch(square -> square.getRow() == 5 && square.getColumn() == 3);
    }

    @Test
    void enPassenMoveShouldBeAvailable_andPieceShouldBeRemoved_white() {
        board.movePiece(6, 2, 4, 2);

        board.movePiece(1, 6, 2, 6);

        board.movePiece(4, 2, 3, 2);

        board.movePiece(1, 1, 3, 1);

        board.movePiece(3, 2, 2, 1);


        Piece piece = board.getSquares()[2][1].getPiece();
        Piece removedPiece = board.getSquares()[3][1].getPiece();


        assertThat(piece.getColour()).isEqualTo("white");
        assertThat(piece).isExactlyInstanceOf(Pawn.class);

        assertThat(removedPiece).isExactlyInstanceOf(EmptyPiece.class);
    }

    @Test
    void enPassenMoveShouldBeAvailable_andPieceShouldBeRemoved_black() {
        board.movePiece(6, 6, 5, 6);

        board.movePiece(1, 2, 3, 2);

        board.movePiece(6, 7, 5, 7);

        board.movePiece(3, 2, 4, 2);

        board.movePiece(6, 3, 4, 3);

        board.movePiece(4, 2, 5, 3);

        Piece piece = board.getSquares()[5][3].getPiece();
        Piece removedPiece = board.getSquares()[4][3].getPiece();


        assertThat(piece.getColour()).isEqualTo("black");
        assertThat(piece).isExactlyInstanceOf(Pawn.class);

        assertThat(removedPiece).isExactlyInstanceOf(EmptyPiece.class);
    }

    @Test
    void enPassenMoveShouldBeAvailableWhenPieceOnTheEdgeColumn0_white() {
        board.movePiece(6, 0, 4, 0);

        board.movePiece(1, 6, 2, 6);

        board.movePiece(4, 0, 3, 0);

        board.movePiece(1, 1, 3, 1);

        Piece piece = board.getSquares()[3][0].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 1);
    }

    @Test
    void enPassenMoveShouldBeAvailableWhenPieceOnTheEdgeColumn0_black() {
        board.movePiece(6, 6, 5, 6);

        board.movePiece(1, 0, 3, 0);

        board.movePiece(6, 7, 5, 7);

        board.movePiece(3, 0, 4, 0);

        board.movePiece(6, 1, 4, 1);

        Piece piece = board.getSquares()[4][0].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 5 && square.getColumn() == 1);
    }

    @Test
    void enPassenMoveShouldBeAvailableWhenPieceOnTheEdgeColumn7_white() {
        board.movePiece(6, 7, 4, 7);

        board.movePiece(1, 0, 2, 0);

        board.movePiece(4, 7, 3, 7);

        board.movePiece(1, 6, 3, 6);

        Piece piece = board.getSquares()[3][7].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 6);
    }

    @Test
    void enPassenMoveShouldBeAvailableWhenPieceOnTheEdgeColumn7_black() {
        board.movePiece(6, 0, 5, 0);

        board.movePiece(1, 7, 3, 7);

        board.movePiece(6, 1, 5, 1);

        board.movePiece(3, 7, 4, 7);

        board.movePiece(6, 6, 4, 6);

        Piece piece = board.getSquares()[4][7].getPiece();

        assertThat(piece.getLegalMoves())
            .anyMatch(square -> square.getRow() == 5 && square.getColumn() == 6);
    }

    @Test
    void enPassenMoveShouldNotBeAvailableIfItPutsOwnKingInCheck_vertical_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(7, 3, 3, 0),
            new ChessMoveDto(7, 7, 4, 6),
            new ChessMoveDto(0, 7, 4, 7)
        );
        Board board = new Board(preInitChessMoveDtos);

        board.movePiece(6, 2, 4, 2);

        board.movePiece(4, 7, 3, 7);

        board.movePiece(4, 2, 3, 2);

        board.movePiece(1, 1, 3, 1);


        Piece piece = board.getSquares()[3][2].getPiece();

        assertThat(piece.getLegalMoves())
            .noneMatch(square -> square.getRow() == 2 && square.getColumn() == 1);
    }

    @Test
    void enPassenMoveShouldNotBeAvailableIfItPutsOwnKingInCheck_horizontal_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(7, 3, 2, 2),
            new ChessMoveDto(0, 0, 5, 2),
            new ChessMoveDto(6, 2, 4, 2)
        );
        Board board = new Board(preInitChessMoveDtos);

        board.movePiece(4, 2, 3, 2);

        board.movePiece(1, 1, 3, 1);

        Piece piece = board.getSquares()[3][2].getPiece();

        assertThat(piece.getLegalMoves())
            .noneMatch(square -> square.getRow() == 2 && square.getColumn() == 1);
    }

    @Test
    void enPassenMoveShouldNotBeAvailableIfItPutsOwnKingInCheck_vertical_black() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(0, 3, 4, 0),
            new ChessMoveDto(7, 7, 4, 7),
            new ChessMoveDto(1, 2, 3, 2)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(6, 7, 5, 7);

        board.movePiece(3, 2, 4, 2);

        board.movePiece(6, 3, 4, 3);

        Piece piece = board.getSquares()[4][2].getPiece();

        assertThat(piece.getLegalMoves())
            .noneMatch(square -> square.getRow() == 5 && square.getColumn() == 3);
    }
}
