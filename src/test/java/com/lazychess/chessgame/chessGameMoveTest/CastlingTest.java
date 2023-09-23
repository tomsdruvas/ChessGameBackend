package com.lazychess.chessgame.chessGameMoveTest;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.CastlingHasMoved;
import com.lazychess.chessgame.chessgame.EmptyPiece;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Rook;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveRequest;

class CastlingTest {

    private Board board;

    @BeforeEach
    public void loadChess() {
        board = new Board();
    }

    @Test
    void kingsAndRooksAtTheStartShouldAllHaveHasMovedAsFalse() {
        List<Piece> allPieceImplementingHasMoved = stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof CastlingHasMoved)
            .toList();


        assertThat(allPieceImplementingHasMoved).hasSize(6).allSatisfy(piece -> {
            assertThat(piece).isInstanceOfAny(King.class, Rook.class);
            assertThat(((CastlingHasMoved)piece).getHasMoved()).isFalse();
        });
    }

    @Test
    void kingPieceShouldHaveHasMovedAsTrueIfItHasMoved() {
        List<ChessMoveRequest> preInitChessMoveRequests = List.of(
            new ChessMoveRequest(1, 3, 3, 3),
            new ChessMoveRequest(6, 3, 4, 3)
        );
        Board board = new Board(preInitChessMoveRequests);
        board.movePiece(7,3,6,3);
        board.movePiece(0,3,1,3);

        List<Piece> allKings = stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof King)
            .toList();

        assertThat(allKings).hasSize(2).allSatisfy(piece -> assertThat(((CastlingHasMoved)piece).getHasMoved()).isTrue());
    }

    @Test
    void rookPieceShouldHaveHasMovedAsTrueIfItHasMoved() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(1, 0, 2, 0),
            new ChessMoveRequest(1, 7, 2, 7),
            new ChessMoveRequest(6, 0, 5, 0),
            new ChessMoveRequest(6, 7, 5, 7)
        );
        Board board = new Board(preInitChessMoveRequestList);

        board.movePiece(7,0,6,0);
        board.movePiece(0,0,1,0);
        board.movePiece(7,7,6,7);
        board.movePiece(0,7,1,7);

        List<Piece> allRooks = stream(board.getSquares())
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece instanceof Rook)
            .toList();

        assertThat(allRooks).hasSize(4).allSatisfy(piece -> assertThat(((CastlingHasMoved) piece).getHasMoved()).isTrue());
    }

    @Test
    void castlingShouldBeAvailableOnBothSides_whenAllCriteriaIsMet_whiteKing() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 5, 1),
            new ChessMoveRequest(7, 2, 5, 2),

            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);


        Piece whiteKing = board.getSquares()[7][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 5);
    }

    @Test
    void castlingShouldBeAvailableOnBothSides_whenAllCriteriaIsMet_blackKing() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 2, 1),
            new ChessMoveRequest(0, 2, 2, 2),

            new ChessMoveRequest(0, 4, 2, 4),
            new ChessMoveRequest(0, 5, 2, 5),
            new ChessMoveRequest(0, 6, 2, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);

        Piece whiteKing = board.getSquares()[0][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 5);
    }

    @Test
    void castlingShouldBeAvailableOnBothSides_whenAllCriteriaIsMet_whiteKing_twoSide() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 5, 1),
            new ChessMoveRequest(7, 2, 5, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);


        Piece whiteKing = board.getSquares()[7][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 1);
    }

    @Test
    void castlingShouldBeAvailableOnBothSides_whenAllCriteriaIsMet_whiteKing_3side() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);


        Piece whiteKing = board.getSquares()[7][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 5);
    }

    @Test
    void castlingShouldBeAvailableOnBothSides_whenAllCriteriaIsMet_blackKing_twoSide() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 2, 1),
            new ChessMoveRequest(0, 2, 2, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);

        Piece whiteKing = board.getSquares()[0][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 1);
    }

    @Test
    void castlingShouldBeAvailableOnBothSides_whenAllCriteriaIsMet_blackKing_threeSide() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 4, 2, 4),
            new ChessMoveRequest(0, 5, 2, 5),
            new ChessMoveRequest(0, 6, 2, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);

        Piece whiteKing = board.getSquares()[0][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 5);
    }

    @Test
    void afterMakingCastlingMove_rookAndKingShouldBothMoveInOneGo_white2Side() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 5, 1),
            new ChessMoveRequest(7, 2, 5, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(7,3,7,1);


        Piece emptySquare = board.getSquares()[7][0].getPiece();
        Piece whiteKing = board.getSquares()[7][1].getPiece();
        Piece whiteRook = board.getSquares()[7][2].getPiece();
        Piece emptySquare2 = board.getSquares()[7][3].getPiece();

        assertThat(emptySquare).isExactlyInstanceOf(EmptyPiece.class);
        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteRook).isExactlyInstanceOf(Rook.class);
        assertThat(emptySquare2).isExactlyInstanceOf(EmptyPiece.class);
    }

    @Test
    void afterMakingCastlingMove_rookAndKingShouldBothMoveInOneGo_white3Side() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(7,3,7,5);


        Piece emptySquare = board.getSquares()[7][3].getPiece();
        Piece whiteRook = board.getSquares()[7][4].getPiece();
        Piece whiteKing = board.getSquares()[7][5].getPiece();
        Piece emptySquare2 = board.getSquares()[7][7].getPiece();

        assertThat(emptySquare).isExactlyInstanceOf(EmptyPiece.class);
        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteRook).isExactlyInstanceOf(Rook.class);
        assertThat(emptySquare2).isExactlyInstanceOf(EmptyPiece.class);
    }

    @Test
    void afterMakingCastlingMove_rookAndKingShouldBothMoveInOneGo_black2Side() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 2, 1),
            new ChessMoveRequest(0, 2, 2, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(0,3,0,1);


        Piece emptySquare = board.getSquares()[0][0].getPiece();
        Piece blackKing = board.getSquares()[0][1].getPiece();
        Piece blackRook = board.getSquares()[0][2].getPiece();
        Piece emptySquare2 = board.getSquares()[0][3].getPiece();

        assertThat(emptySquare).isExactlyInstanceOf(EmptyPiece.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);
        assertThat(blackRook).isExactlyInstanceOf(Rook.class);
        assertThat(emptySquare2).isExactlyInstanceOf(EmptyPiece.class);
    }

    @Test
    void afterMakingCastlingMove_rookAndKingShouldBothMoveInOneGo_black3Side() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 4, 2, 4),
            new ChessMoveRequest(0, 5, 2, 5),
            new ChessMoveRequest(0, 6, 2, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(0,3,0,5);


        Piece emptySquare = board.getSquares()[0][3].getPiece();
        Piece blackRook = board.getSquares()[0][4].getPiece();
        Piece blackKing = board.getSquares()[0][5].getPiece();
        Piece emptySquare2 = board.getSquares()[0][7].getPiece();

        assertThat(emptySquare).isExactlyInstanceOf(EmptyPiece.class);
        assertThat(blackKing).isExactlyInstanceOf(King.class);
        assertThat(blackRook).isExactlyInstanceOf(Rook.class);
        assertThat(emptySquare2).isExactlyInstanceOf(EmptyPiece.class);
    }

    @Test
    void castlingShouldNotBeAvailable_whenPiecesAreBetweenKingAndRook_whiteKing() {

        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 5, 1),

            new ChessMoveRequest(7, 4, 5, 4)
        );
        Board board = new Board(preInitChessMoveRequestList);

        assertCastlingNotAvailableForWhiteKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenPiecesAreBetweenKingAndRook_blackKing() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 2, 1),

            new ChessMoveRequest(0, 4, 2, 4)
        );
        Board board = new Board(preInitChessMoveRequestList);

        assertCastlingNotAvailableForBlackKing(board);

    }

    @Test
    void castlingShouldNotBeAvailable_whenKingIsInCheck_whiteKing() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 5, 1),
            new ChessMoveRequest(7, 2, 5, 2),

            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6),

            new ChessMoveRequest(6, 3, 3, 6),
            new ChessMoveRequest(0, 0, 4, 3)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(1,7,2,7);

        assertCastlingNotAvailableForWhiteKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingIsInCheck_blackKing() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 2, 1),
            new ChessMoveRequest(0, 2, 2, 2),

            new ChessMoveRequest(0, 4, 2, 4),
            new ChessMoveRequest(0, 5, 2, 5),
            new ChessMoveRequest(0, 6, 2, 6),

            new ChessMoveRequest(1, 3, 3, 6),
            new ChessMoveRequest(7, 0, 3, 3)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);

        assertCastlingNotAvailableForBlackKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_whiteKing_sideWith2Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 3, 7),
            new ChessMoveRequest(7, 2, 5, 2),

            new ChessMoveRequest(6, 1, 4, 6),
            new ChessMoveRequest(0, 0, 4, 1)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(1,7,2,7);

        assertCastlingNotAvailableForWhiteKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_blackKing_sideWith2Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 2, 7),
            new ChessMoveRequest(0, 2, 2, 6),

            new ChessMoveRequest(1, 1, 3, 6),
            new ChessMoveRequest(7, 0, 3, 1)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);

        assertCastlingNotAvailableForBlackKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_whiteKing_sideWith3Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6),

            new ChessMoveRequest(6, 5, 4, 6),
            new ChessMoveRequest(0, 0, 5, 5)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(1,7,2,7);

        assertCastlingNotAvailableForWhiteKing(board);

    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_blackKing_sideWith3Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 4, 2, 0),
            new ChessMoveRequest(0, 5, 2, 1),
            new ChessMoveRequest(0, 6, 2, 2),

            new ChessMoveRequest(1, 5, 3, 6),
            new ChessMoveRequest(7, 0, 3, 5)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);

        assertCastlingNotAvailableForBlackKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingIsGoingThroughCheck_whiteKing_sideWith2Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 5, 6),
            new ChessMoveRequest(7, 2, 5, 5),

            new ChessMoveRequest(6, 2, 4, 6),
            new ChessMoveRequest(0, 0, 4, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(1,7,2,7);

        assertCastlingNotAvailableForWhiteKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingIsGoingThroughCheck_blackKing_sideWith2Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 2, 6),
            new ChessMoveRequest(0, 2, 2, 5),

            new ChessMoveRequest(1, 2, 3, 6),
            new ChessMoveRequest(7, 0, 3, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);

        assertCastlingNotAvailableForBlackKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingIsGoingThroughCheck_whiteKing_sideWith3Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 4, 5, 0),
            new ChessMoveRequest(7, 5, 5, 1),
            new ChessMoveRequest(7, 6, 5, 2),

            new ChessMoveRequest(6, 4, 4, 6),
            new ChessMoveRequest(0, 0, 4, 4)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(1,7,2,7);

        assertCastlingNotAvailableForWhiteKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingIsGoingThroughCheck_blackKing_sideWith3Spaces() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 4, 2, 0),
            new ChessMoveRequest(0, 5, 2, 1),
            new ChessMoveRequest(0, 6, 2, 2),

            new ChessMoveRequest(1, 4, 3, 6),
            new ChessMoveRequest(7, 0, 3, 4)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);

        assertCastlingNotAvailableForBlackKing(board);
    }

    @Test
    void castlingShouldNotBeAvailable_whenRookOrKingHasBeenMoved_andTheyAreInTheirOriginalPosition() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 3, 7),
            new ChessMoveRequest(7, 2, 5, 2),

            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(7,3,7,2);
        board.movePiece(1,7,2,7);
        board.movePiece(7,2,7,3);

        Piece whiteKing = board.getSquares()[7][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .noneMatch(square -> square.getRow() == 7 && square.getColumn() == 1)
            .noneMatch(square -> square.getRow() == 7 && square.getColumn() == 5);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_whiteKing_sideWith2Spaces_andShouldBeAvailableOnOtherSide() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 1, 3, 7),
            new ChessMoveRequest(7, 2, 5, 2),

            new ChessMoveRequest(6, 1, 4, 6),
            new ChessMoveRequest(0, 0, 4, 1),

            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(1,7,2,7);

        Piece whiteKing = board.getSquares()[7][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .noneMatch(square -> square.getRow() == 7 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 5);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_blackKing_sideWith2Spaces_andShouldBeAvailableOnOtherSide() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 3, 7),
            new ChessMoveRequest(0, 2, 3, 6),

            new ChessMoveRequest(1, 1, 4, 6),
            new ChessMoveRequest(7, 0, 3, 1),

            new ChessMoveRequest(0, 4, 2, 4),
            new ChessMoveRequest(0, 5, 2, 5),
            new ChessMoveRequest(0, 6, 2, 6)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);

        Piece blackKing = board.getSquares()[0][3].getPiece();

        assertThat(blackKing).isExactlyInstanceOf(King.class);
        assertThat(blackKing.getLegalMoves())
            .noneMatch(square -> square.getRow() == 0 && square.getColumn() == 1)
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 5);
    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_whiteKing_sideWith3Spaces_andShouldBeAvailableOnOtherSide() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(7, 4, 5, 4),
            new ChessMoveRequest(7, 5, 5, 5),
            new ChessMoveRequest(7, 6, 5, 6),

            new ChessMoveRequest(6, 5, 4, 6),
            new ChessMoveRequest(0, 0, 5, 5),

            new ChessMoveRequest(7, 1, 5, 1),
            new ChessMoveRequest(7, 2, 5, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);
        board.movePiece(1,7,2,7);

        Piece whiteKing = board.getSquares()[7][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 7 && square.getColumn() == 1)
            .noneMatch(square -> square.getRow() == 7 && square.getColumn() == 5);

    }

    @Test
    void castlingShouldNotBeAvailable_whenKingWillEndUpInCheck_blackKing_sideWith3Spaces_andShouldBeAvailableOnOtherSide() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 4, 2, 0),
            new ChessMoveRequest(0, 5, 2, 1),
            new ChessMoveRequest(0, 6, 2, 2),

            new ChessMoveRequest(1, 5, 3, 6),
            new ChessMoveRequest(7, 0, 3, 5),

            new ChessMoveRequest(0, 1, 2, 1),
            new ChessMoveRequest(0, 2, 2, 2)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,7,5,7);

        Piece blackKing = board.getSquares()[0][3].getPiece();

        assertThat(blackKing).isExactlyInstanceOf(King.class);
        assertThat(blackKing.getLegalMoves())
            .anyMatch(square -> square.getRow() == 0 && square.getColumn() == 1)
            .noneMatch(square -> square.getRow() == 0 && square.getColumn() == 5);
    }

    @Test
    void castlingShouldNotBeAvailable_whenPawnIsInFrontOfRook() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 1, 5, 7),
            new ChessMoveRequest(0, 2, 5, 6),
            new ChessMoveRequest(1, 0, 5, 5),
            new ChessMoveRequest(6, 0, 1, 0)
        );
        Board board = new Board(preInitChessMoveRequestList);
        board.movePiece(6,1,5,1);

        Piece blackKing = board.getSquares()[0][3].getPiece();

        assertThat(blackKing).isExactlyInstanceOf(King.class);
        assertThat(blackKing.getLegalMoves())
            .noneMatch(square -> square.getRow() == 0 && square.getColumn() == 1);
    }

    private static void assertCastlingNotAvailableForWhiteKing(Board board) {
        Piece whiteKing = board.getSquares()[7][3].getPiece();

        assertThat(whiteKing).isExactlyInstanceOf(King.class);
        assertThat(whiteKing.getLegalMoves())
            .noneMatch(square -> square.getRow() == 7 && square.getColumn() == 1)
            .noneMatch(square -> square.getRow() == 7 && square.getColumn() == 5);
    }

    private static void assertCastlingNotAvailableForBlackKing(Board board) {
        Piece blackKing = board.getSquares()[0][3].getPiece();

        assertThat(blackKing).isExactlyInstanceOf(King.class);
        assertThat(blackKing.getLegalMoves())
            .noneMatch(square -> square.getRow() == 0 && square.getColumn() == 1)
            .noneMatch(square -> square.getRow() == 0 && square.getColumn() == 5);
    }
}
