package com.lazychess.chessgame.chessGameMoveITTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.PreInitialisationMoveDto;

@SpringBootTest
class QueenTest {

    private static Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void bothQueensShouldHaveZeroLegalMovesWhenInitiated() {
        List<Piece> queens = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(this::findBothQueensByTheirStartingPosition)
            .toList();

        assertThat(queens).hasSize(2).allSatisfy(piece -> {
            assertThat(piece).isExactlyInstanceOf(Queen.class);
            assertThat(piece.getLegalMoves()).isEmpty();
        });
    }

    @Test
    void whenPawnMoves_bothQueensShouldHaveOneLegalMoveWhen() {
        board.movePiece(6,3,5,3);
        board.movePiece(1,3,2,3);

        List<Piece> queens = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Queen)
            .toList();

        assertThat(queens).hasSize(2).allSatisfy(piece -> {
            assertThat(piece.getLegalMoves()).hasSize(1);
        });
    }

    @Test
    void whenPawnMoves_bothQueensShouldHaveNineLegalMoveWhen() {
        board.movePiece(6,3,4,3);
        board.movePiece(1,3,3,3);
        board.movePiece(6,2,5,2);
        board.movePiece(1,2,2,2);
        board.movePiece(6,4,5,4);
        board.movePiece(1,4,2,4);

        List<Piece> queens = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Queen)
            .toList();

        assertThat(queens).hasSize(2).allSatisfy(piece -> {
            assertThat(piece.getLegalMoves()).hasSize(9);
        });
    }

    @Test
    void queenShouldBeAbleToTakeOppositePieceDiagonally() {
        board.movePiece(6,3,4,3);
        board.movePiece(1,3,3,3);

        board.movePiece(6,2,5,2);
        board.movePiece(1,2,2,2);
        board.movePiece(6,4,5,4);
        board.movePiece(1,4,2,4);

        board.movePiece(7,3,5,5);
        board.movePiece(0,3,2,5);

        board.movePiece(5,5,3,3);
        board.movePiece(2,5,4,3);

        List<Piece> queens = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Queen)
            .toList();

        List<Piece> allPawns = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Pawn)
            .toList();

        assertThat(queens).hasSize(2).allSatisfy(piece -> assertThat(piece.getLegalMoves()).hasSize(17));
        assertThat(allPawns).hasSize(14);
    }

    @Test
    void queenShouldBeAbleToTakeOppositePieceStraight() {
        board.movePiece(6,4,4,4);
        board.movePiece(1,5,2,5);
        board.movePiece(7,3,5,5);
        board.movePiece(2,5,3,5);
        board.movePiece(5,5,3,5);

        Piece queen = board.getSquares()[3][5].getPiece();

        List<Piece> allPawns = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Pawn)
            .toList();

        assertThat(allPawns).hasSize(15);
        assertThat(queen).isExactlyInstanceOf(Queen.class);
    }

    @Test
    void queenShouldBeAbleToTakeOppositeKingDiagonally() {
        List<PreInitialisationMoveDto> preInitialisationMoveDtos = List.of(
            new PreInitialisationMoveDto(7, 3, 4, 6),
            new PreInitialisationMoveDto(1, 5, 2, 5)
        );

        Board board = new Board(preInitialisationMoveDtos);
        board.movePiece(4,6,3,7);

        Piece queen = board.getSquares()[3][7].getPiece();
        assertThat(queen).isExactlyInstanceOf(Queen.class);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }

    @Test
    void queenShouldBeAbleToTakeOppositeKingStraight() {
        board.movePiece(6,4,5,4);
        board.movePiece(1,4,2,4);
        board.movePiece(7,3,4,6);
        board.movePiece(2,4,3,4);
        board.movePiece(4,6,4,4);
        board.movePiece(1,0,2,0);
        board.movePiece(4,4,3,4);

        Piece queen = board.getSquares()[3][4].getPiece();
        assertThat(queen).isExactlyInstanceOf(Queen.class);
        assertThat(board.getStateOfTheGame()).isSameAs(ChessGameState.CHECKMATE);
    }

    private boolean findBothQueensByTheirStartingPosition(Piece piece) {
        return
            (piece.getPieceRow() == 0 && piece.getPieceColumn() == 3) || (piece.getPieceRow() == 7 && piece.getPieceColumn() == 3);
    }
}
