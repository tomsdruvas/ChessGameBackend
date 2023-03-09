package com.lazychess.chessgame.chessgame;

import static com.lazychess.chessgame.chessgame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessgame.ChessConstants.WHITE;
import static com.lazychess.chessgame.chessgame.ChessGameState.CHECKMATE;
import static com.lazychess.chessgame.chessgame.ChessGameState.ONGOING;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;
import org.springframework.util.SerializationUtils;

import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.dto.IllegalMovesDto;
import com.lazychess.chessgame.exception.EmptySourceSquareException;
import com.lazychess.chessgame.exception.GameIsNotInOnGoingStateException;
import com.lazychess.chessgame.exception.IllegalMoveException;
import com.lazychess.chessgame.exception.NotYourTurnException;
import com.lazychess.chessgame.exception.WrongColourPieceOnSquareException;

public class Board {

    private Square[][] squares;
    Piece whiteRook1;
    Piece whiteRook2;
    Piece whiteKnight1;
    Piece whiteKnight2;
    Piece whiteBishop1;
    Piece whiteBishop2;
    Piece whiteQueen;
    Piece whiteKing;
    Piece[] whitePawn;


    Piece blackRook1;
    Piece blackRook2;
    Piece blackKnight1;
    Piece blackKnight2;
    Piece blackBishop1;
    Piece blackBishop2;
    Piece blackQueen;
    Piece blackKing;
    Piece[] blackPawn;

    private ChessGameState stateOfTheGame = ONGOING;
    private String currentPlayerColourState = WHITE;

    public Board(List<ChessMoveDto> chessMoveDtoList) {
        this.squares = new Square[8][8];
        loadSquares();
        loadPieces();
        makePreInitialisationMoves(chessMoveDtoList);
        loadPieceLegalMoves(squares);
    }

    public Board() {
        this.squares = new Square[8][8];
        loadSquares();
        loadPieces();
        loadPieceLegalMoves(squares);
    }

    public void loadSquares() {
        boolean squareColour = true;

        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                squares[i][j] = new Square(i,j,squareColour);
                squareColour = !squareColour;
            }
        }

    }

    public void loadPieces() {
        whiteRook1 = new Rook("White Rook1",7,0,WHITE);
        whiteRook2 = new Rook("White Rook2",7,7,WHITE);
        whiteKnight1 = new Knight("White Knight1",7,1,WHITE);
        whiteKnight2 = new Knight("White Knight2",7,6,WHITE);
        whiteBishop1 = new Bishop("White Bishop1",7,2,WHITE);
        whiteBishop2 = new Bishop("White Bishop2",7,5,WHITE);
        whiteQueen = new Queen("White Queen",7,4,WHITE);
        whiteKing = new King("White King",7,3,WHITE);

        whitePawn = new Piece[8];
        for(int i=0;i<8;i++)
        {
            whitePawn[i] = new Pawn("White Pawn"+(i+1),6 ,i,WHITE);
        }

        blackRook1 = new Rook("Black Rook1",0,0,BLACK);
        blackRook2 = new Rook("Black Rook2",0,7,BLACK);
        blackKnight1 = new Knight("Black Knight1",0,1,BLACK);
        blackKnight2 = new Knight("Black Knight2",0,6,BLACK);
        blackBishop1 = new Bishop("Black Bishop1",0,2,BLACK);
        blackBishop2 = new Bishop("Black Bishop2",0,5,BLACK);
        blackQueen = new Queen("Black Queen",0,4,BLACK);
        blackKing = new King("Black King",0,3,BLACK);

        blackPawn = new Piece[8];
        for(int i=0;i<8;i++)
        {
            blackPawn[i] = new Pawn("Black Pawn"+(i+1),1,i,BLACK);
        }

        squares[7][0].setPiece(whiteRook1);
        squares[7][1].setPiece(whiteKnight1);
        squares[7][2].setPiece(whiteBishop1);
        squares[7][3].setPiece(whiteKing);
        squares[7][4].setPiece(whiteQueen);
        squares[7][5].setPiece(whiteBishop2);
        squares[7][6].setPiece(whiteKnight2);
        squares[7][7].setPiece(whiteRook2);

        for(int i=0;i<8;i++)
        {
            squares[6][i].setPiece(whitePawn[i]);
        }

        squares[0][0].setPiece(blackRook1);
        squares[0][1].setPiece(blackKnight1);
        squares[0][2].setPiece(blackBishop1);
        squares[0][3].setPiece(blackKing);
        squares[0][4].setPiece(blackQueen);
        squares[0][5].setPiece(blackBishop2);
        squares[0][6].setPiece(blackKnight2);
        squares[0][7].setPiece(blackRook2);

        for(int i=0;i<8;i++)
        {
            squares[1][i].setPiece(blackPawn[i]);
        }
    }

    public Piece getPieceByName(String name) {
        Square square1 = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> square.getPiece() != null)
            .filter(square -> Objects.equals(square.getPiece().getName(), name)).toList().stream().findFirst().orElseThrow();

        return square1.getPiece();
    }

    public Square[][] getSquares() {
        return squares;
    }

    public void movePiece(int currentRow, int currentColumn, int newRow, int newColumn) {
        checkIfGameIsOnGoing();

        Piece pieceToMove = squares[currentRow][currentColumn].getPiece();
        List<LegalMoveSquare> legalMoves = pieceToMove.getLegalMoves();
        String currentPlayersColour = pieceToMove.getColour();

        checkIfSourceSquareIsEmpty(pieceToMove);
        checkIfItIsColoursTurn(pieceToMove);
        checkIfSourceSquareHasCurrentPlayersPieceOnIt(currentRow, currentColumn);

        if(isMoveLegal(legalMoves, newRow, newColumn)) {
            pieceToMove.setPieceRow(newRow);
            pieceToMove.setPieceColumn(newColumn);
            squares[newRow][newColumn].setPiece(pieceToMove);
            squares[currentRow][currentColumn].setPiece(new EmptyPiece());
            ifMoveIsACastlingMoveAlsoMoveRook(pieceToMove, newRow, newColumn);

            loadPieceLegalMoves(squares);

            List<LegalMoveSquare> squaresTheKingIsInDanger = listOfSquaresWhereOppositeKingIsInDanger(currentPlayersColour, squares);
            setOppositeKingsLegalMovesToPreventCheckMateOnItself(currentPlayersColour);

            if (!squaresTheKingIsInDanger.isEmpty()) {
                clearLegalMovesOfAllPiecesApartFromKingWhenItIsInDanger(currentPlayersColour);
                clearCastlingMovesFromKingWhenItIsInDanger(currentPlayersColour);
                checkIfOppositeKingIsInCheckMate(currentPlayersColour, squares);
            }
            else {
                removeLegalMovesThatPutKingInDanger(currentPlayersColour);
//                checkIfCastlingMovesAreAvailable(currentPlayersColour);
                removeCastlingMovesWhereKingIsGoingThroughACheckOrEndUpInCheck(currentPlayersColour);
            }

            setOppositeColourAsCurrentPlayer();
            checkIfItIsKingsOrRooksFirstMove(pieceToMove);
        }
        else {
            throw new IllegalMoveException("That is not a legal move for a " + pieceToMove.getClass().getSimpleName());
        }
    }

    private void checkIfCastlingMovesAreAvailable(String currentPlayersColour) {
        Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> square.getPiece() != null)
            .filter(square -> !square.getPiece().getColour().equals(currentPlayersColour))
            .filter(square -> square.getPiece() instanceof King)
            .toList().stream().findFirst().orElseThrow().getPiece().generateLegalMoves(squares);
    }

    private void ifMoveIsACastlingMoveAlsoMoveRook(Piece pieceToMove, int newRow, int newColumn) {
        if (pieceToMove instanceof King king && !king.getHasMoved()) {
            if(pieceToMove.getPieceRow() == 7 && newRow == 7 && newColumn == 1) {
                moveRookAsPartOfCastling(7,0,7,2);
            }

            if(pieceToMove.getPieceRow() == 7 && newRow == 7 && newColumn == 5) {
                moveRookAsPartOfCastling(7,7,7,4);
            }

            if(pieceToMove.getPieceRow() == 0 && newRow == 0 && newColumn == 1) {
                moveRookAsPartOfCastling(0,0,0,2);
            }

            if(pieceToMove.getPieceRow() == 0 && newRow == 0 && newColumn == 5) {
                moveRookAsPartOfCastling(0,7,0,4);
            }
        }
    }

    private void moveRookAsPartOfCastling(int rookCurrentRow, int rookCurrentColumn, int rookNewRow, int rookNewColumn) {
        Piece rookToMove = squares[rookCurrentRow][rookCurrentColumn].getPiece();
        rookToMove.setPieceRow(rookNewRow);
        rookToMove.setPieceColumn(rookNewColumn);
        squares[rookNewRow][rookNewColumn].setPiece(rookToMove);
        squares[rookCurrentRow][rookCurrentColumn].setPiece(new EmptyPiece());

    }

    private void removeCastlingMovesWhereKingIsGoingThroughACheckOrEndUpInCheck(String currentPlayersColour) {
        Piece oppositePlayersKingPiece = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> square.getPiece() != null)
            .filter(square -> !square.getPiece().getColour().equals(currentPlayersColour))
            .filter(square -> square.getPiece() instanceof King)
            .toList().stream().findFirst().orElseThrow().getPiece();

        List<LegalMoveSquare> allCurrentPlayerMoves = Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .filter(square -> square.getPiece().getColour().equals(currentPlayersColour))
            .flatMap(square -> square.getPiece().getLegalMoves().stream())
            .toList();

        if(oppositePlayersKingPiece.getPieceRow() == 7 && !((King) oppositePlayersKingPiece).getHasMoved()) {
            boolean castlingWhiteSquare1 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 7 && square.getColumn() == 1);
            boolean castlingWhiteSquare2 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 7 && square.getColumn() == 2);
            if(castlingWhiteSquare1 || castlingWhiteSquare2) {
                oppositePlayersKingPiece.removeLegalMove(7,1);
            }
            boolean castlingWhiteSquare3 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 7 && square.getColumn() == 4);
            boolean castlingWhiteSquare4 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 7 && square.getColumn() == 5);
            if(castlingWhiteSquare3 || castlingWhiteSquare4) {
                oppositePlayersKingPiece.removeLegalMove(7,5);
            }
        }

        if(oppositePlayersKingPiece.getPieceRow() == 0 && !((King) oppositePlayersKingPiece).getHasMoved()) {
            boolean castlingBlackSquare1 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 0 && square.getColumn() == 1);
            boolean castlingBlackSquare2 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 0 && square.getColumn() == 2);
            if(castlingBlackSquare1 || castlingBlackSquare2) {
                oppositePlayersKingPiece.removeLegalMove(0,1);
            }

            boolean castlingBlackSquare3 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 0 && square.getColumn() == 4);
            boolean castlingBlackSquare4 = allCurrentPlayerMoves.stream().anyMatch(square -> square.getRow() == 0 && square.getColumn() == 5);

            if(castlingBlackSquare3 || castlingBlackSquare4) {
                oppositePlayersKingPiece.removeLegalMove(0,5);
            }
        }
    }

    private void checkIfItIsKingsOrRooksFirstMove(Piece pieceToMove) {
        if(pieceToMove instanceof CastlingHasMoved piece && (!piece.getHasMoved())) {
            piece.hasMoved();
        }
    }

    private void checkIfSourceSquareHasCurrentPlayersPieceOnIt(int currentRow, int currentColumn) {
        String colour = getSquares()[currentRow][currentColumn].getPiece().getColour();
        if (Objects.equals(colour, "empty")) {
            throw new EmptySourceSquareException("Source square does not have a piece on it");
        } else if (!Objects.equals(colour, getCurrentPlayerColourState())) {
            throw new WrongColourPieceOnSquareException("Source square does not have your colour piece on it");
        }
    }

    private void setOppositeColourAsCurrentPlayer() {
        if (Objects.equals(getCurrentPlayerColourState(), WHITE)) {
            setCurrentPlayerColourState(BLACK);
        } else if (Objects.equals(getCurrentPlayerColourState(), BLACK)) {
            setCurrentPlayerColourState(WHITE);
        }
    }

    private void checkIfItIsColoursTurn(Piece pieceOnSquare) {
        String colour = pieceOnSquare.getColour();
        if(!Objects.equals(getCurrentPlayerColourState(), colour)) {
            throw new NotYourTurnException("It is not the " + colour +"'s turn");
        }
    }

    private void checkIfSourceSquareIsEmpty(Piece piece) {
        if(piece instanceof EmptyPiece) {
            throw new EmptySourceSquareException("Source square does not have a piece on it");
        }
    }

    private void loadPieceLegalMoves(Square[][] squares) {
        Arrays.stream(squares).forEach(pieces -> Arrays.stream(pieces).forEach(square -> square.getPiece().generateLegalMoves(squares)));
    }

    private void removeLegalMovesThatPutKingInDanger(String colour) {

        List<IllegalMovesDto> illegalMovesDtos = Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> !piece.getColour().equals(colour))
            .filter(piece -> piece.getLegalMoves() != null)
            .filter(piece -> !piece.getLegalMoves().isEmpty())
            .map(piece -> new IllegalMovesDto(
                piece.getName(),
                findLegalOwnMovesThatCheckKing(piece, colour)
            ))
            .filter(illegalMovesDto -> !illegalMovesDto.illegalMoves().isEmpty())
            .toList();

        illegalMovesDtos
            .forEach(illegalMovesDto -> getPieceByName(illegalMovesDto.pieceName()).getLegalMoves().forEach(square -> {
                if(illegalMovesDto.illegalMoves().contains(square)){
                    getPieceByName(illegalMovesDto.pieceName()).removeLegalMove(square.getRow(), square.getColumn());
                }
            }));
    }

    private List<LegalMoveSquare> findLegalOwnMovesThatCheckKing(Piece piece, String colour) {

        return piece.getLegalMoves().stream().filter(square -> {

            int currentPieceRow = piece.getPieceRow();
            int currentPieceColumn = piece.getPieceColumn();
            Piece clearedSquarePiece = SerializationUtils.clone(squares[piece.getPieceRow()][piece.getPieceColumn()].getPiece());
            Piece movedOnToPiece = SerializationUtils.clone(squares[square.getRow()][square.getColumn()].getPiece());

            squares[square.getRow()][square.getColumn()].setPiece(piece);
            squares[piece.getPieceRow()][piece.getPieceColumn()].clearPiece();
            piece.setPieceRow(square.getRow());
            piece.setPieceColumn(square.getColumn());

            loadPieceLegalMoves(squares);
            List<LegalMoveSquare> listOfSquaresWhereKingIsInDanger = listOfSquaresWhereOppositeKingIsInDanger(colour, squares);

            squares[square.getRow()][square.getColumn()].setPiece(movedOnToPiece);
            squares[clearedSquarePiece.getPieceRow()][clearedSquarePiece.getPieceColumn()].setPiece(clearedSquarePiece);
            piece.setPieceRow(currentPieceRow);
            piece.setPieceColumn(currentPieceColumn);
            loadPieceLegalMoves(squares);

            return !listOfSquaresWhereKingIsInDanger.isEmpty();
        })
            .toList();
    }

    private boolean isMoveLegal(List<LegalMoveSquare> legalMoves, int newRow, int newColumn) {
        return legalMoves.stream().anyMatch(square -> square.getRow() == newRow && square.getColumn() == newColumn);
    }

    private List<LegalMoveSquare> listOfSquaresWhereOppositeKingIsInDanger(String colour, Square[][] squares) {
        return Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .filter(square -> square.getPiece().getColour().equals(colour))
            .filter(square -> square.getPiece().getLegalMoves()!=null)
            .flatMap(square -> square.getPiece().getLegalMoves().stream())
            .filter(square -> square.getPiece() instanceof King)
            .toList();
    }

    private void clearLegalMovesOfAllPiecesApartFromKingWhenItIsInDanger(String colour) {
        Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .filter(square -> !square.getPiece().getColour().equals(colour))
            .filter(square -> square.getPiece().getLegalMoves() != null)
            .filter(square -> !(square.getPiece() instanceof King))
            .forEach(square -> square.getPiece().clearLegalMoves());
    }

    private void clearCastlingMovesFromKingWhenItIsInDanger(String colour) {
        Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .filter(square -> !square.getPiece().getColour().equals(colour))
            .filter(square -> square.getPiece() instanceof King)
            .forEach(square -> ((King) square.getPiece()).removeCastlingMoves());
    }

    private void setOppositeKingsLegalMovesToPreventCheckMateOnItself(String colour) {
        Piece kingPiece = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> square.getPiece() != null)
            .filter(square -> !square.getPiece().getColour().equals(colour))
            .filter(square -> square.getPiece() instanceof King)
            .toList().stream().findFirst().orElseThrow().getPiece();

        List<LegalMoveSquare> listOfPossibleMovesByNextPlayer = listOfPossibleMovesByNextPlayer(colour);
        List<LegalMoveSquare> pawnStraightMoves = pawnStraightMoves(colour);
        List<LegalMoveSquare> pawnDiagonalLegalMovesWhereKingCannotGo = pawnDiagonalMoves(colour);

        List<LegalMoveSquare> listOfPossibleMovesByNextPlayerWithoutPawnStraightMoves = ListUtils.subtract(listOfPossibleMovesByNextPlayer, pawnStraightMoves);
        List<LegalMoveSquare> listOfPossibleMovesByNextPlayerWithoutPawnStraightMovesAndWithPawnDiagonalMoves = ListUtils.union(listOfPossibleMovesByNextPlayerWithoutPawnStraightMoves, pawnDiagonalLegalMovesWhereKingCannotGo);

        List<LegalMoveSquare> kingLegalMoves = kingPiece.getLegalMoves();

        List<LegalMoveSquare> kingLegalMovesWithoutDanger = ListUtils.subtract(kingLegalMoves, listOfPossibleMovesByNextPlayerWithoutPawnStraightMovesAndWithPawnDiagonalMoves);

        kingPiece.setLegalMoves(kingLegalMovesWithoutDanger);
    }

    private List<LegalMoveSquare> listOfPossibleMovesByNextPlayer(String colour) {
        return Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .filter(square -> square.getPiece().getColour().equals(colour))
            .filter(square -> square.getPiece().getLegalMoves() != null)
            .flatMap(square -> square.getPiece().getLegalMoves().stream())
            .toList();
    }

    private List<LegalMoveSquare> pawnStraightMoves(String colour) {
        return Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece.getColour().equals(colour))
            .filter(piece -> piece.getLegalMoves()!=null)
            .filter(Pawn.class::isInstance)
            .flatMap(piece -> ((Pawn) piece).generateStraightLegalMoves().stream())
            .toList();
    }

    private List<LegalMoveSquare> pawnDiagonalMoves(String colour) {
        return Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> piece.getColour().equals(colour))
            .filter(piece -> piece.getLegalMoves()!=null)
            .filter(Pawn.class::isInstance)
            .flatMap(piece -> ((Pawn) piece).getDiagonalLegalMovesToPreventTheKingFromGoingIntoCheckMate(squares).stream())
            .toList();
    }

    public List<Piece> getAllPieces() {
        return Arrays.stream(squares)
            .flatMap(Arrays::stream)
            .map(Square::getPiece)
            .filter(piece -> !(piece instanceof EmptyPiece))
            .toList();
    }

    public ChessGameState getStateOfTheGame() {
        return stateOfTheGame;
    }

    public void setStateOfTheGame(ChessGameState stateOfTheGame) {
        this.stateOfTheGame = stateOfTheGame;
    }

    private void checkIfGameIsOnGoing() {
        if(getStateOfTheGame() != ONGOING) {
            throw new GameIsNotInOnGoingStateException("The game is not in the ongoing state");
        }
    }

    private void checkIfOppositeKingIsInCheckMate(String currentPlayersColour, Square[][] squares) {
        Piece kingPiece = Arrays.stream(squares).flatMap(Arrays::stream)
            .filter(square -> square.getPiece() != null)
            .filter(square -> !square.getPiece().getColour().equals(currentPlayersColour))
            .filter(square -> square.getPiece() instanceof King)
            .toList().stream().findFirst().orElseThrow().getPiece();

        if(kingPiece.getLegalMoves().isEmpty()) {
            setStateOfTheGame(CHECKMATE);
        }
    }

    public String getCurrentPlayerColourState() {
        return currentPlayerColourState;
    }

    public void setCurrentPlayerColourState(String currentPlayerColourState) {
        this.currentPlayerColourState = currentPlayerColourState;
    }

    public void makePreInitialisationMoves(List<ChessMoveDto> chessMoveDtoList) {
        chessMoveDtoList.forEach(chessMoveDto -> {
            Piece pieceToMove = squares[chessMoveDto.currentRow()][chessMoveDto.currentColumn()].getPiece();
            pieceToMove.setPieceColumn(chessMoveDto.newColumn());
            pieceToMove.setPieceRow(chessMoveDto.newRow());
            squares[chessMoveDto.newRow()][chessMoveDto.newColumn()].setPiece(pieceToMove);
            squares[chessMoveDto.currentRow()][chessMoveDto.currentColumn()].setPiece(new EmptyPiece());
        });
    }

    public void setSquares(Square[][] squares) {
        this.squares = squares;
    }


}
