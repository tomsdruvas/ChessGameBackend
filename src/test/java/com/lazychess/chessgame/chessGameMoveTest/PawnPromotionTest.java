package com.lazychess.chessgame.chessGameMoveTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lazychess.chessgame.chessgame.Bishop;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.Knight;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Rook;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.exception.InvalidChessPieceForPawnPromotionException;
import com.lazychess.chessgame.exception.WrongColourPieceOnSquareException;

class PawnPromotionTest {

    @BeforeEach
    public void loadChess() {
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

        assertThatThrownBy(() -> board.movePiece(1,6,2,6)).isInstanceOf(WrongColourPieceOnSquareException.class)
            .hasMessage("Source square does not have your colour piece on it");
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

        assertThatThrownBy(() -> board.movePiece(6,4,5,4)).isInstanceOf(WrongColourPieceOnSquareException.class)
            .hasMessage("Source square does not have your colour piece on it");
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
    void pawnPromotionPendingPropertyOnBoardShouldFalse_andHaveExtraBishop_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(0, 0, 2, 7),
            new ChessMoveDto(1, 0, 3, 7),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(1, 0, 0, 0);

        board.promoteAPawn("Bishop");

        List<Piece> allWhiteBishops = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Bishop)
            .filter(piece -> Objects.equals(piece.getColour(), "white"))
            .toList();

        assertThat(board.getCurrentPlayerColourState()).isEqualTo("black");
        assertThat(board.isPawnPromotionPending()).isFalse();
        assertThat(allWhiteBishops).hasSize(3);
    }

    @Test
    void pawnPromotionPendingPropertyOnBoardShouldFalse_andHaveExtraRook_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(0, 0, 2, 7),
            new ChessMoveDto(1, 0, 3, 7),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(1, 0, 0, 0);

        board.promoteAPawn("Rook");

        List<Piece> allWhiteRooks = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Rook)
            .filter(piece -> Objects.equals(piece.getColour(), "white"))
            .toList();

        assertThat(board.getCurrentPlayerColourState()).isEqualTo("black");
        assertThat(board.isPawnPromotionPending()).isFalse();
        assertThat(allWhiteRooks).hasSize(3);
    }

    @Test
    void pawnPromotionPendingPropertyOnBoardShouldFalse_andHaveExtraKnight_white() {
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(0, 0, 2, 7),
            new ChessMoveDto(1, 0, 3, 7),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board board = new Board(preInitChessMoveDtos);
        board.movePiece(1, 0, 0, 0);

        board.promoteAPawn("Knight");

        List<Piece> allWhiteKnights = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Knight)
            .filter(piece -> Objects.equals(piece.getColour(), "white"))
            .toList();

        assertThat(board.getCurrentPlayerColourState()).isEqualTo("black");
        assertThat(board.isPawnPromotionPending()).isFalse();
        assertThat(allWhiteKnights).hasSize(3);
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
