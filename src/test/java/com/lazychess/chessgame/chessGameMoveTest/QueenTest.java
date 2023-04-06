package com.lazychess.chessgame.chessGameMoveTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;

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
        board.movePiece(6,4,5,4);
        board.movePiece(1,4,2,4);

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
        board.movePiece(6,4,4,4);
        board.movePiece(1,4,3,4);
        board.movePiece(6,3,5,3);
        board.movePiece(1,3,2,3);
        board.movePiece(6,5,5,5);
        board.movePiece(1,5,2,5);

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
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 5, 5, 6),
            new ChessMoveDto(6, 5, 2, 6)
        );

        Board board = new Board(preInitChessMoveDtoList);

        Piece blackQueen = board.getSquares()[0][4].getPiece();
        Piece whiteQueen = board.getSquares()[7][4].getPiece();
        Piece whitePawn = board.getSquares()[2][6].getPiece();
        Piece blackPawn = board.getSquares()[5][6].getPiece();


        assertThat(whiteQueen).isExactlyInstanceOf(Queen.class);
        assertThat(blackQueen).isExactlyInstanceOf(Queen.class);
        assertThat(whitePawn).isExactlyInstanceOf(Pawn.class);
        assertThat(blackPawn).isExactlyInstanceOf(Pawn.class);

        List<Piece> queens = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Queen)
            .toList();

        assertThat(queens).hasSize(2).allSatisfy(piece -> assertThat(piece.getLegalMoves()).hasSize(2));

        board.movePiece(7,4,5,6);
        board.movePiece(0,4,2,6);

        List<Piece> allPawns = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Pawn)
            .toList();

        assertThat(allPawns).hasSize(14);
    }

    @Test
    void queenShouldBeAbleToTakeOppositePieceStraight() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 4, 5, 4),
            new ChessMoveDto(6, 4, 2, 4)
        );

        Board board = new Board(preInitChessMoveDtoList);

        Piece blackQueen = board.getSquares()[0][4].getPiece();
        Piece whiteQueen = board.getSquares()[7][4].getPiece();
        Piece whitePawn = board.getSquares()[2][4].getPiece();
        Piece blackPawn = board.getSquares()[5][4].getPiece();


        assertThat(whiteQueen).isExactlyInstanceOf(Queen.class);
        assertThat(blackQueen).isExactlyInstanceOf(Queen.class);
        assertThat(whitePawn).isExactlyInstanceOf(Pawn.class);
        assertThat(blackPawn).isExactlyInstanceOf(Pawn.class);

        List<Piece> queens = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Queen)
            .toList();

        assertThat(queens).hasSize(2).allSatisfy(piece -> assertThat(piece.getLegalMoves()).hasSize(2));

        board.movePiece(7,4,5,4);
        board.movePiece(0,4,2,4);

        List<Piece> allPawns = Arrays.stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Pawn)
            .toList();

        assertThat(allPawns).hasSize(14);
    }

    private boolean findBothQueensByTheirStartingPosition(Piece piece) {
        return
            (piece.getPieceRow() == 0 && piece.getPieceColumn() == 4) || (piece.getPieceRow() == 7 && piece.getPieceColumn() == 4);
    }
}
