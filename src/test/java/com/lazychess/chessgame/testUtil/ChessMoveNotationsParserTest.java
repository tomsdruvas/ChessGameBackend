package com.lazychess.chessgame.testUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.chessgame.Bishop;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Knight;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Rook;

@SpringBootTest
class ChessMoveNotationsParserTest {

    Map<String, Integer> mapRow = Map.of(
        "1",7,
        "2", 6,
        "3", 5,
        "4", 4,
        "5", 3,
        "6", 2,
        "7", 1,
        "8", 0);

    Map<String, Integer> mapColumn = Map.of(
        "a",7,
        "b", 6,
        "c", 5,
        "d", 4,
        "e", 3,
        "f", 2,
        "g", 1,
        "h", 0);

    Map<String, Class<? extends Piece>> pieceTypeMap = Map.of(
        "Q", Queen.class,
        "N", Knight.class,
        "B", Bishop.class,
        "K", King.class,
        "P", Pawn.class,
        "R", Rook.class);


    @Test
    void parseChessNotions() {
        String chessNotationsString = "1.e4 c5 2.c4 Nc6 3.Nc3 g6 4.d4 cxd4 5.Nce2 Bg7 6.b3 e5 7.Bb2 Nf6 8.Nf3 Nxe4 9.Nxe5 Bxe5 10.Nxd4 Qa5+ 11.b4 Qxb4+ 12.Ke2 Qxb2+ 13.Kf3 Qxf2+ 14.Kxe4 d5+ 15.Kxd5 Be6+ 16.Kc5 Bxd4+ 17.Kd6 O-O-O";
        String substring = chessNotationsString.substring(2);
        String[] splitStringList = substring.split("\\.");

        for (int i = 0; i < splitStringList.length; i++) {

            String suffix = String.valueOf(i + 2);
            boolean b = splitStringList[i].endsWith(suffix);

            if(b) {
                for(int j = 0; j <= suffix.length(); j++) {
                    splitStringList[i] = StringUtils.chop(splitStringList[i]);
                }
            }
        }

        List<String> listOfMovesAsString = Arrays.stream(splitStringList).map(item -> item.split(" ")).flatMap(Arrays::stream).toList();

        List<ChessMoveNotion> chessMoveNotions = IntStream.range(0, listOfMovesAsString.size())
            .mapToObj(index -> buildChessMoveNotation(index, listOfMovesAsString.get(index))).toList();


    }

    private ChessMoveNotion buildChessMoveNotation(int index, String moveItemAsString) {
        boolean shouldEndInCheck = moveItemAsString.endsWith("+");
        boolean shouldTakePiece = moveItemAsString.contains("x");

        String pieceColour;
        int row = 0;
        int column = 0;

        if(shouldEndInCheck) {
            moveItemAsString = StringUtils.chop(moveItemAsString);
        }

        if(shouldTakePiece) {
            moveItemAsString = moveItemAsString.replace("x", "");
        }

        if(index % 2 == 0) {
            pieceColour = "white";
        }
        else {
            pieceColour = "black";
        }

        if (moveItemAsString.length() == 2) {
            row = mapRow.get(String.valueOf(moveItemAsString.charAt(1)));
            column = mapColumn.get(String.valueOf(moveItemAsString.charAt(0)));
        }

        ChessMoveNotion.builder()
            .pieceColour(pieceColour)
            .shouldEndInCheck(shouldEndInCheck)
            .shouldTakePiece(shouldTakePiece)
            .row(row)
            .column(column)


//            new ChessMoveNotion(, ,shouldEndInCheck)
    }

}
