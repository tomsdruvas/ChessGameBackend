package com.lazychess.chessgame.chessGameMoveITTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.chessgame.EmptyPiece;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Rook;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;

@SpringBootTest
class RookTest {

    private static Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void allRooksShouldHaveZeroLegalMovesWhenInitiated() {
        List<Piece> allRooks = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(this::findAllRooksByTheirStartingPosition)
            .filter(piece -> !(piece instanceof EmptyPiece))
            .toList();

        assertThat(allRooks).hasSize(4).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(Rook.class);
            assertThat(piece.getLegalMoves()).isEmpty();
        });
    }

    @Test
    void whenAllPawnsInFrontOfRooksMove_allRooksShouldHaveTwoLegalMoves() {
        board.movePiece(6,0,4,0);
        board.movePiece(1,7,3,7);
        board.movePiece(6,7,4,7);
        board.movePiece(1,0,3,0);

        List<Piece> allRooks = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(this::findAllRooksByTheirStartingPosition)
            .filter(piece -> !(piece instanceof EmptyPiece))
            .toList();

        assertThat(allRooks).hasSize(4).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(Rook.class);
            assertThat(piece.getLegalMoves()).hasSize(2);
        });
    }

    @Test
    void whenAllPawnsInFrontOfRooksMove_andRooksMove_allRooksShouldHaveEightLegalMoves() {
        board.movePiece(6,0,4,0);
        board.movePiece(1,7,3,7);
        board.movePiece(6,7,4,7);
        board.movePiece(1,0,3,0);

        board.movePiece(7,0,5,0);
        board.movePiece(0,7,2,7);
        board.movePiece(7,7,5,7);
        board.movePiece(0,0,2,0);

        List<Piece> allRooks = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Rook)
            .toList();

        assertThat(allRooks).hasSize(4).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(Rook.class);
            assertThat(piece.getLegalMoves()).hasSize(8);
        });
    }

    @Test
    void whenAllPawnsInFrontOfRooksMove_andRooksMoveTwice_allRooksShouldHaveEightLegalMoves() {
        board.movePiece(6,0,4,0);
        board.movePiece(1,7,3,7);
        board.movePiece(6,7,4,7);
        board.movePiece(1,0,3,0);

        board.movePiece(7,0,5,0);
        board.movePiece(0,7,2,7);
        board.movePiece(7,7,5,7);
        board.movePiece(0,0,2,0);

        board.movePiece(5,0,5,1);
        board.movePiece(2,7,2,6);
        board.movePiece(5,7,5,6);
        board.movePiece(2,0,2,1);

        List<Piece> allRooks = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Rook)
            .toList();

        assertThat(allRooks).hasSize(4).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(Rook.class);
            assertThat(piece.getLegalMoves()).hasSize(8);
        });
    }

    @Test
    void whenAllPawnsInFrontOfRooksMove_andRooksMoveTwice_andRooksTakeOppositePawns_thereShouldTwoLessRooks() {
        board.movePiece(6,0,4,0);
        board.movePiece(1,7,3,7);
        board.movePiece(6,7,4,7);
        board.movePiece(1,0,3,0);

        board.movePiece(7,0,5,0);
        board.movePiece(0,7,2,7);
        board.movePiece(7,7,5,7);
        board.movePiece(0,0,2,0);


        board.movePiece(5,0,5,1);
        board.movePiece(2,7,2,6);
        board.movePiece(5,7,5,6);
        board.movePiece(2,0,2,1);


        board.movePiece(5,6,2,6);
        board.movePiece(2,1,5,1);

        List<Piece> allRooks = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Rook)
            .toList();

        assertThat(allRooks).hasSize(2).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(Rook.class);
            assertThat(piece.getLegalMoves()).hasSize(11);
        });
    }

    @Test
    void rookShouldNotBeAbleToPutOwnKingInCheck() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 3, 3, 7),
            new ChessMoveDto(0, 0, 3, 3),
            new ChessMoveDto(7, 0, 5, 2)
        );

        Board board = new Board(preInitChessMoveDtoList);
        board.movePiece(5,2,5,3);

        Piece whiteRook = board.getSquares()[5][3].getPiece();
        Piece blackRook = board.getSquares()[3][3].getPiece();
        Piece blackKing= board.getSquares()[0][3].getPiece();

        assertThat(whiteRook).isExactlyInstanceOf(Rook.class);
        assertThat(blackRook).isExactlyInstanceOf(Rook.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        assertThat(blackRook.getLegalMoves())
            .noneMatch(square -> square.getRow() == 3 && square.getColumn() == 0)
            .noneMatch(square -> square.getRow() == 3 && square.getColumn() == 1)
            .noneMatch(square -> square.getRow() == 3 && square.getColumn() == 2)
            .noneMatch(square -> square.getRow() == 3 && square.getColumn() == 4)
            .noneMatch(square -> square.getRow() == 3 && square.getColumn() == 5)
            .noneMatch(square -> square.getRow() == 3 && square.getColumn() == 6)
            .noneMatch(square -> square.getRow() == 3 && square.getColumn() == 7)
            .anyMatch(square -> square.getRow() == 2 && square.getColumn() == 3)
            .anyMatch(square -> square.getRow() == 1 && square.getColumn() == 3)
            .anyMatch(square -> square.getRow() == 4 && square.getColumn() == 3)
            .anyMatch(square -> square.getRow() == 5 && square.getColumn() == 3);
    }

    @Test
    void rookShouldBeAbleToTakeOppositeKingCheckMate() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 3, 3, 7),
            new ChessMoveDto(7, 0, 5, 2)
        );

        Board board = new Board(preInitChessMoveDtoList);
        board.movePiece(5,2,5,3);

        Piece whiteRook = board.getSquares()[5][3].getPiece();
        Piece blackKing= board.getSquares()[0][3].getPiece();

        assertThat(whiteRook).isExactlyInstanceOf(Rook.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        List<Piece> allPieces = board.getAllPieces();
        assertThat(allPieces)
            .filteredOn(piece -> Objects.equals(piece.getColour(), "black") && !(piece instanceof King))
            .hasSize(15).allSatisfy(piece -> {
                assertThat(piece.getLegalMoves()).isEmpty();
            });

        assertThat(whiteRook)
            .isExactlyInstanceOf(Rook.class)
            .satisfies(piece -> assertThat(Objects.equals(piece.getColour(), "white")).isTrue())
            .satisfies(piece -> assertThat(piece.getLegalMoves()).anyMatch(square -> square.getRow() == 0 && square.getColumn() == 3));

        assertThat(blackKing.getLegalMoves()).isEmpty();

        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }

    @Test
    void rookShouldBeAbleToTakeOppositeKingNotCheckMate() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 3, 3, 7),
            new ChessMoveDto(1, 2, 2, 2),
            new ChessMoveDto(1, 4, 2, 4),
            new ChessMoveDto(7, 0, 5, 2)
        );

        Board board = new Board(preInitChessMoveDtoList);
        board.movePiece(5,2,5,3);

        Piece whiteRook = board.getSquares()[5][3].getPiece();
        Piece blackKing= board.getSquares()[0][3].getPiece();

        assertThat(whiteRook).isExactlyInstanceOf(Rook.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        List<Piece> allPieces = board.getAllPieces();
        assertThat(allPieces)
            .filteredOn(piece -> Objects.equals(piece.getColour(), "black") && !(piece instanceof King))
            .hasSize(15).allSatisfy(piece -> {
                assertThat(piece.getLegalMoves()).isEmpty();
            });

        assertThat(whiteRook)
            .isExactlyInstanceOf(Rook.class)
            .satisfies(piece -> assertThat(Objects.equals(piece.getColour(), "white")).isTrue())
            .satisfies(piece -> assertThat(piece.getLegalMoves()).anyMatch(square -> square.getRow() == 0 && square.getColumn() == 3));

        assertThat(blackKing.getLegalMoves()).hasSize(2)
            .anyMatch(square -> square.getRow() == 1 && square.getColumn() == 4)
            .anyMatch(square -> square.getRow() == 1 && square.getColumn() == 2);

        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.ONGOING);
    }

    private boolean findAllRooksByTheirStartingPosition(Piece piece) {
        return
            (piece.getPieceRow() == 7 && piece.getPieceColumn() == 7) ||
                (piece.getPieceRow() == 0 && piece.getPieceColumn() == 0) ||
                (piece.getPieceRow() == 0 && piece.getPieceColumn() == 7) ||
                (piece.getPieceRow() == 7 && piece.getPieceColumn() == 0);
    }
}
