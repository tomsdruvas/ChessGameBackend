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
        board.movePiece(6,6,5,6);
        board.movePiece(1,4,3,4);
        board.movePiece(6,0,4,0);
        board.movePiece(0,4,1,4);
        board.movePiece(7,0,5,0);
        board.movePiece(1,4,2,4);
        board.movePiece(5,0,5,1);
        board.movePiece(3,4,4,4);
        board.movePiece(6,5,5,5);
        board.movePiece(2,4,3,4);
        board.movePiece(5,1,4,1);
        board.movePiece(1,0,3,0);
        board.movePiece(6,7,5,7);
        board.movePiece(0,0,2,0);
        board.movePiece(5,7,4,7);
        board.movePiece(2,0,2,3);
        board.movePiece(6,3,5,3);
        board.movePiece(2,3,3,3);
        board.movePiece(4,1,3,1);

        Piece whiteRook = board.getSquares()[3][1].getPiece();
        Piece blackRook = board.getSquares()[3][3].getPiece();
        Piece blackKing= board.getSquares()[3][4].getPiece();

        assertThat(whiteRook).isExactlyInstanceOf(Rook.class);
        assertThat(blackRook).isExactlyInstanceOf(Rook.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        assertThat(blackRook.getLegalMoves())
            .noneMatch(square -> square.getRow() == 2 && square.getColumn() == 3)
            .noneMatch(square -> square.getRow() == 4 && square.getColumn() == 3)
            .noneMatch(square -> square.getRow() == 5 && square.getColumn() == 3);
    }

    @Test
    void rookShouldBeAbleToTakeOppositeKingCheckMate() {
        board.movePiece(6,7,4,7);
        board.movePiece(1,4,2,4);
        board.movePiece(7,7,5,7);
        board.movePiece(2,4,3,4);
        board.movePiece(5,7,5,4);
        board.movePiece(1,0,2,0);
        board.movePiece(5,4,3,4);

        List<Piece> allPieces = board.getAllPieces();
        assertThat(allPieces)
            .filteredOn(piece -> Objects.equals(piece.getColour(), "black") && !(piece instanceof King))
            .hasSize(14).allSatisfy(piece -> {
            assertThat(piece.getLegalMoves()).isEmpty();
        });

        Piece blackKing = board.getSquares()[0][4].getPiece();
        assertThat(blackKing.getLegalMoves()).isEmpty();
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        Piece whiteRook = board.getSquares()[3][4].getPiece();
        assertThat(whiteRook)
            .isExactlyInstanceOf(Rook.class)
            .satisfies(piece -> assertThat(Objects.equals(piece.getColour(), "white")).isTrue())
            .satisfies(piece -> assertThat(piece.getLegalMoves()).anyMatch(square -> square.getRow() == 0 && square.getColumn() == 4));
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }


    @Test
    void rookShouldBeAbleToTakeOppositeKingNotCheckMate() {
        board.movePiece(6,7,4,7);
        board.movePiece(1,4,2,4);
        board.movePiece(7,7,5,7);
        board.movePiece(1,5,2,5);
        board.movePiece(5,7,5,4);
        board.movePiece(1,0,2,0);
        board.movePiece(5,4,2,4);

        List<Piece> allPieces = board.getAllPieces();
        assertThat(allPieces)
            .filteredOn(piece -> Objects.equals(piece.getColour(), "black") && !(piece instanceof King))
            .hasSize(14).allSatisfy(piece -> {
                assertThat(piece.getLegalMoves()).isEmpty();
            });

        Piece blackKing = board.getSquares()[0][4].getPiece();
        assertThat(blackKing.getLegalMoves()).hasSize(1);
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        Piece whiteRook = board.getSquares()[2][4].getPiece();
        assertThat(whiteRook)
            .isExactlyInstanceOf(Rook.class)
            .satisfies(piece -> assertThat(Objects.equals(piece.getColour(), "white")).isTrue())
            .satisfies(piece -> assertThat(piece.getLegalMoves()).anyMatch(square -> square.getRow() == 0 && square.getColumn() == 4));
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
