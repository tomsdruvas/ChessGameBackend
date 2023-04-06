package com.lazychess.chessgame.chessGameMoveTest;

import static com.lazychess.chessgame.chessgame.ChessConstants.EMPTY_PIECE;
import static com.lazychess.chessgame.chessgame.ChessConstants.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Bishop;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;

@SpringBootTest
class PawnTest {

    private static Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void allPawnsShouldHaveTwoLegalMovesAtTheStart() {
        for (int i = 0; i < 8; i++) {
            Piece whitePawn = board.getSquares()[6][i].getPiece();
            List<LegalMoveSquare> legalMovesForWhitePawn = whitePawn.getLegalMoves();
            assertThat(legalMovesForWhitePawn).hasSize(2);

            assertThat(legalMovesForWhitePawn).allSatisfy(square -> {
                assertThat(square.getColumn()).isEqualTo(whitePawn.getPieceColumn());
                assertThat(square.getRow()).isNotEqualTo(whitePawn.getPieceRow());
            });
        }

        for (int i = 0; i < 8; i++) {
            Piece blackPawn = board.getSquares()[1][i].getPiece();
            List<LegalMoveSquare> legalMovesForBlackPawn = blackPawn.getLegalMoves();
            assertThat(legalMovesForBlackPawn).hasSize(2);

            assertThat(legalMovesForBlackPawn).allSatisfy(square -> {
                assertThat(square.getColumn()).isEqualTo(blackPawn.getPieceColumn());
                assertThat(square.getRow()).isNotEqualTo(blackPawn.getPieceRow());
            });
        }
    }

    @Test
    void afterFirstMoveWhitePawnShouldOnlyBeAbleToMoveOneSpaceForward() {
        board.movePiece(6,1,4,1);
        List<LegalMoveSquare> legalMoves = board.getSquares()[4][1].getPiece().getLegalMoves();

        assertThat(legalMoves).hasSize(1);
        assertThat(legalMoves.get(0).getRow()).isEqualTo(3);
        assertThat(legalMoves.get(0).getColumn()).isEqualTo(1);
    }

    @Test
    void afterFirstMoveBlackPawnShouldOnlyBeAbleToMoveOneSpace() {
        board.movePiece(6,7,5,7);
        board.movePiece(1,6,2,6);
        List<LegalMoveSquare> legalMoves = board.getSquares()[2][6].getPiece().getLegalMoves();

        assertThat(legalMoves).hasSize(1);
        assertThat(legalMoves.get(0).getRow()).isEqualTo(3);
        assertThat(legalMoves.get(0).getColumn()).isEqualTo(6);
    }

    @Test
    void pawnShouldNotBeAbleToTakeAPieceStraight() {
        board.movePiece(6,1,4,1);
        board.movePiece(1,1,3,1);
        List<LegalMoveSquare> legalMovesWhitePawn = board.getSquares()[4][1].getPiece().getLegalMoves();
        List<LegalMoveSquare> legalMovesBlackPawn = board.getSquares()[3][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).isEmpty();
        assertThat(legalMovesBlackPawn).isEmpty();
    }

    @Test
    void pawnShouldBeAbleToTakeAPieceDiagonally() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);

        List<LegalMoveSquare> legalMovesWhitePawn = board.getSquares()[4][2].getPiece().getLegalMoves();
        List<LegalMoveSquare> legalMovesBlackPawn = board.getSquares()[3][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).hasSize(2);
        assertThat(legalMovesBlackPawn).hasSize(2);
    }

    @Test
    void pawnShouldNotBeAbleToTakeAPieceAlongTheColumn() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,2);

        List<LegalMoveSquare> legalMovesWhitePawn = board.getSquares()[3][2].getPiece().getLegalMoves();
        List<LegalMoveSquare> legalMovesBlackPawn = board.getSquares()[3][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).hasSize(1);
        assertThat(legalMovesBlackPawn).hasSize(1);
    }

    @Test
    void pawnShouldNotBeAbleToTakeAPieceDiagonallyBackwards() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,2);
        board.movePiece(3,1,4,1);

        List<LegalMoveSquare> legalMovesWhitePawn = board.getSquares()[3][2].getPiece().getLegalMoves();
        List<LegalMoveSquare> legalMovesBlackPawn = board.getSquares()[4][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).hasSize(1);
        assertThat(legalMovesBlackPawn).hasSize(1);
    }

    @Test
    void pawnShouldBeAbleToTakeOppositeColourPieceDiagonally() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,1);


        Piece whitePawn = board.getSquares()[3][1].getPiece();

        assertThat(whitePawn.getColour()).isEqualTo(WHITE);
        assertThat(Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> !Objects.equals(piece.getColour(), EMPTY_PIECE))
            .toList())
            .hasSize(31);
    }

    @Test
    void pawnShouldNotBeAbleToMoveOnToItsOwnColour() {
        board.movePiece(6,2,4,2);
        board.movePiece(1,1,3,1);
        board.movePiece(4,2,3,1);
        board.movePiece(1,7,2,7);
        board.movePiece(6,1,4,1);

        List<LegalMoveSquare> legalMovesWhitePawn = board.getSquares()[4][1].getPiece().getLegalMoves();

        assertThat(legalMovesWhitePawn).isEmpty();
    }

    @Test
    void pawnShouldNotBeAbleToTakeBackwards() {
        board.movePiece(6,0,4,0);
        board.movePiece(1,1,3,1);
        board.movePiece(4,0,3,0);
        board.movePiece(1,7,2,7);
        board.movePiece(7,0,4,0);
        board.movePiece(3,1,4,0);

        List<LegalMoveSquare> legalMovesWhitePawn = board.getSquares()[4][0].getPiece().getLegalMoves();


        assertThat(legalMovesWhitePawn).allSatisfy(square -> {
            assertThat(square.getColumn()).isZero();
            assertThat(square.getRow()).isEqualTo(5);

        }).hasSize(1);

        assertThat(Arrays.stream(board.getSquares()).flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> !Objects.equals(piece.getColour(), EMPTY_PIECE))
            .toList())
            .hasSize(31);
    }

    @Test
    void pawnCannotMakeAMoveThatPutsOwnKingInCheck() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(7, 2, 4, 5)
        );

        Board board = new Board(preInitChessMoveDtoList);
        board.movePiece(4,5,3,6);

        Piece whiteBishop = board.getSquares()[3][6].getPiece();
        Piece blackPawn = board.getSquares()[1][4].getPiece();
        Piece blackKing= board.getSquares()[0][3].getPiece();

        assertThat(whiteBishop).isExactlyInstanceOf(Bishop.class);
        assertThat(blackPawn).isExactlyInstanceOf(Pawn.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);

        assertThat(blackPawn.getLegalMoves())
            .noneMatch(square -> square.getRow() == 2 && square.getColumn() == 4);
    }

    @Test
    void pawnCannotMoveIfAPieceIsInFrontOfItWhite() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(0, 0, 5, 0)
        );
        Board board = new Board(preInitChessMoveDtoList);
        Piece whitePawn = board.getSquares()[6][0].getPiece();

        assertThat(whitePawn).isExactlyInstanceOf(Pawn.class);

        assertThat(whitePawn.getLegalMoves()).isEmpty();
    }

    @Test
    void pawnCannotMoveIfAOwnPieceIsInFrontOfItWhite() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(7, 1, 5, 0)
        );
        Board board = new Board(preInitChessMoveDtoList);
        Piece whitePawn = board.getSquares()[6][0].getPiece();

        assertThat(whitePawn).isExactlyInstanceOf(Pawn.class);

        assertThat(whitePawn.getLegalMoves()).isEmpty();
    }

    @Test
    void pawnCannotMoveIfAPieceIsInFrontOfItBlack() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(7, 0, 2, 0)
        );
        Board board = new Board(preInitChessMoveDtoList);
        Piece blackPawn = board.getSquares()[1][0].getPiece();

        assertThat(blackPawn).isExactlyInstanceOf(Pawn.class);

        assertThat(blackPawn.getLegalMoves()).isEmpty();
    }

    @Test
    void pawnCannotMoveIfAOwnPieceIsInFrontOfItBlack() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(0, 1, 2, 0)
        );
        Board board = new Board(preInitChessMoveDtoList);
        Piece blackPawn = board.getSquares()[1][0].getPiece();

        assertThat(blackPawn).isExactlyInstanceOf(Pawn.class);

        assertThat(blackPawn.getLegalMoves()).isEmpty();
    }
}
