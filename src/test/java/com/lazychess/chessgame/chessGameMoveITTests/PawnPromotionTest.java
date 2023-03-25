package com.lazychess.chessgame.chessGameMoveITTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Rook;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.exception.InvalidChessPieceForPawnPromotionException;
import com.lazychess.chessgame.exception.NotYourTurnException;

@SpringBootTest
class PawnPromotionTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void pawnPromotionPendingPropertyOnBoardShouldTrue_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(0, 0, 2, 7),
            new ChessMoveDto(1, 0, 3, 7),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(1, 0, 0, 0);

        assertThat(board.getCurrentPlayerColourState()).isEqualTo("white");
        assertThat(board.isPawnPromotionPending()).isTrue();

        assertThatThrownBy(() -> board.movePiece(1,6,2,6)).isInstanceOf(NotYourTurnException.class)
            .hasMessage("It is not the black's turn");
    }

    @Test
    void pawnPromotionPendingPropertyOnBoardShouldTrue_black() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(7, 0, 2, 7),
            new ChessMoveDto(6, 0, 3, 7),
            new ChessMoveDto(1, 0, 6, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(6, 7, 5, 7);
        board.movePiece(6, 0, 7, 0);

        assertThat(board.getCurrentPlayerColourState()).isEqualTo("black");
        assertThat(board.isPawnPromotionPending()).isTrue();

        assertThatThrownBy(() -> board.movePiece(6,4,5,4)).isInstanceOf(NotYourTurnException.class)
            .hasMessage("It is not the white's turn");
    }

    @Test
    void pawnPromotionPendingPropertyOnBoardShouldFalse_andHaveExtraQueen_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(0, 0, 2, 7),
            new ChessMoveDto(1, 0, 3, 7),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(1, 0, 0, 0);

        board.promoteAPawn("Queen");

        List<Piece> allWhiteQueens = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Queen)
            .filter(piece -> Objects.equals(piece.getColour(), "white"))
            .toList();

        assertThat(board.getCurrentPlayerColourState()).isEqualTo("black");
        assertThat(board.isPawnPromotionPending()).isFalse();
        assertThat(allWhiteQueens).hasSize(2);
    }

    @Test
    void pawnPromotionPendingPropertyOnBoardShouldFalse_andHaveExtraQueen_black() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(7, 0, 2, 7),
            new ChessMoveDto(6, 0, 3, 7),
            new ChessMoveDto(1, 0, 6, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(6, 7, 5, 7);
        board.movePiece(6, 0, 7, 0);

        board.promoteAPawn("Rook");

        List<Piece> allBlackRooks = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Rook)
            .filter(piece -> Objects.equals(piece.getColour(), "black"))
            .toList();

        assertThat(board.getCurrentPlayerColourState()).isEqualTo("white");
        assertThat(board.isPawnPromotionPending()).isFalse();
        assertThat(allBlackRooks).hasSize(3);
    }

    @Test
    void pawnPromotionShouldThrowException_invalidPromotionPiece_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(0, 0, 2, 7),
            new ChessMoveDto(1, 0, 3, 7),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(1, 0, 0, 0);

        assertThatThrownBy(() -> board.promoteAPawn("testing")).isInstanceOf(InvalidChessPieceForPawnPromotionException.class)
            .hasMessage("testing is not a valid chess piece");
    }

    @Test
    void pawnPromotionShouldThrowException_invalidPromotionPiece_black() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(7, 0, 2, 7),
            new ChessMoveDto(6, 0, 3, 7),
            new ChessMoveDto(1, 0, 6, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(6, 7, 5, 7);
        board.movePiece(6, 0, 7, 0);

        assertThatThrownBy(() -> board.promoteAPawn("testing")).isInstanceOf(InvalidChessPieceForPawnPromotionException.class)
            .hasMessage("testing is not a valid chess piece");
    }
}
