package com.lazychess.chessgame.chessGameMoveITTests;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.CastlingHasMoved;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Rook;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.dto.ChessMoveDto;

@SpringBootTest
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
        List<ChessMoveDto> preInitChessMoveDtos = List.of(
            new ChessMoveDto(1, 3, 3, 3),
            new ChessMoveDto(6, 3, 4, 3)
        );
        Board board = new Board(preInitChessMoveDtos);
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
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 0, 2, 0),
            new ChessMoveDto(1, 7, 2, 7),
            new ChessMoveDto(6, 0, 5, 0),
            new ChessMoveDto(6, 7, 5, 7)
        );

        Board board = new Board(preInitChessMoveDtoList);
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

//    @Test
//    void rookPieceShouldHaveHasMovedAsTrueIfItHasMoved() {
//        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
//            new ChessMoveDto(1, 0, 2, 0),
//            new ChessMoveDto(1, 7, 2, 7),
//            new ChessMoveDto(6, 0, 5, 0),
//            new ChessMoveDto(6, 7, 5, 7)
//        );
//
//        Board board = new Board(preInitChessMoveDtoList);
//        board.movePiece(7,0,6,0);
//        board.movePiece(0,0,1,0);
//        board.movePiece(7,7,6,7);
//        board.movePiece(0,7,1,7);
//
//        List<Piece> allKings = stream(board.getSquares())
//            .flatMap(Arrays::stream)
//            .map(Square::getPiece)
//            .filter(piece -> piece instanceof Rook)
//            .toList();
//
//        assertThat(allKings).hasSize(4).allSatisfy(piece -> assertThat(((CastlingHasMoved)piece).getHasMoved()).isTrue());
//    }
}
