package testUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.lazychess.chessgame.chessgame.Bishop;
import com.lazychess.chessgame.chessgame.King;
import com.lazychess.chessgame.chessgame.Knight;
import com.lazychess.chessgame.chessgame.Pawn;
import com.lazychess.chessgame.chessgame.Piece;
import com.lazychess.chessgame.chessgame.Queen;
import com.lazychess.chessgame.chessgame.Rook;

public class ChessMoveNotationsParser {

    private static final Map<String, Integer> mapRow = Map.of(
        "1",7,
        "2", 6,
        "3", 5,
        "4", 4,
        "5", 3,
        "6", 2,
        "7", 1,
        "8", 0);

    private static final Map<String, Integer> mapColumn = Map.of(
        "a",7,
        "b", 6,
        "c", 5,
        "d", 4,
        "e", 3,
        "f", 2,
        "g", 1,
        "h", 0);

    private static final Map<String, Class<? extends Piece>> pieceTypeMap = Map.of(
        "Q", Queen.class,
        "N", Knight.class,
        "B", Bishop.class,
        "K", King.class,
        "P", Pawn.class,
        "R", Rook.class);


    public static List<ChessMoveNotion> parseChessNotions(String chessNotionString) {
        String substring = chessNotionString.substring(2);
        String[] splitStringList = substring.split("\\.");

        for (int i = 0; i < splitStringList.length; i++) {

            String suffix = String.valueOf(i + 2);
            boolean endsWithIndexNumber = splitStringList[i].endsWith(suffix);
            boolean startsWithSpace = splitStringList[i].startsWith(" ");

            if(endsWithIndexNumber) {
                for(int j = 0; j <= suffix.length(); j++) {
                    splitStringList[i] = StringUtils.chop(splitStringList[i]);
                }
            }
            if(startsWithSpace) {
                splitStringList[i] = splitStringList[i].substring(1);
            }
        }

        List<String> listOfMovesAsString = Arrays.stream(splitStringList).map(item -> item.split(" ")).flatMap(Arrays::stream).toList();

       return IntStream.range(0, listOfMovesAsString.size())
            .mapToObj(index -> buildChessMoveNotation(index, listOfMovesAsString.get(index))).toList();
    }

    private static ChessMoveNotion buildChessMoveNotation(int index, String moveItemAsString) {
        boolean shouldEndInCheck = moveItemAsString.endsWith("+");
        boolean shouldEndInCheckMate = moveItemAsString.endsWith("#");
        boolean shouldTakePiece = moveItemAsString.contains("x");

        String pieceColour = null;
        Class<? extends Piece> pieceToMoveType;
        int pieceLocationColumn = 9;
        int pieceLocationRow = 9;
        int row = 0;
        int column = 0;
        boolean pawnPromotion;
        Class<? extends Piece> pawnPromotionPieceType = null;
        TypeOfMoveEnum typeOfMove;


        if(shouldEndInCheck || shouldEndInCheckMate) {
            moveItemAsString = StringUtils.chop(moveItemAsString);
        }

        if(shouldTakePiece) {
            moveItemAsString = moveItemAsString.replace("x", "");
        }

        if(index % 2 == 0) {
            pieceColour = "white";
        }
        else if (index % 2 == 1) {
            pieceColour = "black";
        }

        boolean firstLetterIsACapital = moveItemAsString.matches("[QBNRK]\\w*");
        boolean firstLetterIsALowercase = moveItemAsString.matches("[abcdefgh]\\w*");

        boolean secondCharIsALowercaseLetter = moveItemAsString.matches("[QBNRK][abcdefgh][abcdefgh][1-8]");
        boolean secondCharIsANumber = moveItemAsString.matches("[QBNRK][1-8][abcdefgh][1-8]");
        pawnPromotion = moveItemAsString.contains("=");

        if (pawnPromotion) {
            if (moveItemAsString.length() == 5) {
                pieceLocationColumn = mapColumn.get(String.valueOf(moveItemAsString.charAt(0)));
                column = mapColumn.get(String.valueOf(moveItemAsString.charAt(1)));
                row = mapRow.get(String.valueOf(moveItemAsString.charAt(2)));
                pawnPromotionPieceType = pieceTypeMap.get(String.valueOf(moveItemAsString.charAt(4)));
                typeOfMove = TypeOfMoveEnum.PAWN_PROMOTION_WITH_COLUMN;
            } else {
                row = mapRow.get(String.valueOf(moveItemAsString.charAt(1)));
                column = mapColumn.get(String.valueOf(moveItemAsString.charAt(0)));
                pawnPromotionPieceType = pieceTypeMap.get(String.valueOf(moveItemAsString.charAt(3)));
                typeOfMove = TypeOfMoveEnum.PAWN_PROMOTION;
            }
            pieceToMoveType = Pawn.class;

        } else if (moveItemAsString.equals("O-O")) {
            typeOfMove = TypeOfMoveEnum.CASTLING;
            pieceToMoveType = King.class;
            if (pieceColour.equals("white")) {
                row = 7;
                column = 1;
            } else if (pieceColour.equals("black")) {
                row = 0;
                column = 1;
            }

        } else if (moveItemAsString.equals("O-O-O")) {
            typeOfMove = TypeOfMoveEnum.CASTLING;
            pieceToMoveType = King.class;
            if (pieceColour.equals("white")) {
                row = 7;
                column = 5;
            } else if (pieceColour.equals("black")) {
                row = 0;
                column = 5;
            }

        } else if (moveItemAsString.length() == 2) {
            typeOfMove = TypeOfMoveEnum.BASIC_PAWN_MOVE;
            pieceToMoveType = Pawn.class;
            row = mapRow.get(String.valueOf(moveItemAsString.charAt(1)));
            column = mapColumn.get(String.valueOf(moveItemAsString.charAt(0)));
        } else if (moveItemAsString.length() == 3 && firstLetterIsACapital) {
            typeOfMove = TypeOfMoveEnum.BASIC_WITH_PIECE_NAME;
            pieceToMoveType = pieceTypeMap.get(String.valueOf(moveItemAsString.charAt(0)));
            row = mapRow.get(String.valueOf(moveItemAsString.charAt(2)));
            column = mapColumn.get(String.valueOf(moveItemAsString.charAt(1)));
        } else if (moveItemAsString.length() == 3 && firstLetterIsALowercase) {
            typeOfMove = TypeOfMoveEnum.PIECE_TO_MOVE_WITH_COLUMN;
            pieceToMoveType = Pawn.class;
            pieceLocationColumn = mapColumn.get(String.valueOf(moveItemAsString.charAt(0)));
            row = mapRow.get(String.valueOf(moveItemAsString.charAt(2)));
            column = mapColumn.get(String.valueOf(moveItemAsString.charAt(1)));
        } else if (moveItemAsString.length() == 4 && firstLetterIsACapital && secondCharIsALowercaseLetter) {
            typeOfMove = TypeOfMoveEnum.PIECE_TO_MOVE_WITH_COLUMN;
            pieceToMoveType = pieceTypeMap.get(String.valueOf(moveItemAsString.charAt(0)));
            pieceLocationColumn = mapColumn.get(String.valueOf(moveItemAsString.charAt(1)));
            row = mapRow.get(String.valueOf(moveItemAsString.charAt(3)));
            column = mapColumn.get(String.valueOf(moveItemAsString.charAt(2)));
        } else if (moveItemAsString.length() == 4 && firstLetterIsACapital && secondCharIsANumber) {
            typeOfMove = TypeOfMoveEnum.PIECE_TO_MOVE_WITH_ROW;
            pieceToMoveType = pieceTypeMap.get(String.valueOf(moveItemAsString.charAt(0)));
            pieceLocationRow = mapRow.get(String.valueOf(moveItemAsString.charAt(1)));
            row = mapRow.get(String.valueOf(moveItemAsString.charAt(3)));
            column = mapColumn.get(String.valueOf(moveItemAsString.charAt(2)));
        } else {
          throw new RuntimeException(moveItemAsString + " not part of any logic");
        }

        return ChessMoveNotion.builder()
            .pieceColour(pieceColour)
            .shouldEndInCheck(shouldEndInCheck)
            .shouldTakePiece(shouldTakePiece)
            .row(row)
            .column(column)
            .pieceClass(pieceToMoveType)
            .pieceLocationColumn(pieceLocationColumn)
            .pieceLocationRow(pieceLocationRow)
            .pawnPromotion(pawnPromotion)
            .pawnPromotionPieceType(pawnPromotionPieceType)
            .typeOfMove(typeOfMove)
            .numberOfMove(index)
            .shouldEndInCheckMate(shouldEndInCheckMate)
            .build();
    }

}
