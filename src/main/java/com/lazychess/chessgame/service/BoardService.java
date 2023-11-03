package com.lazychess.chessgame.service;

import static com.lazychess.chessgame.chessgame.ChessConstants.BLACK;
import static com.lazychess.chessgame.chessgame.ChessConstants.WHITE;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.dto.ChessMoveRequest;
import com.lazychess.chessgame.exception.BoardNotFoundException;
import com.lazychess.chessgame.exception.GameHasFinishedException;
import com.lazychess.chessgame.exception.PawnPromotionStatusNotPendingException;
import com.lazychess.chessgame.exception.PawnPromotionStatusPendingException;
import com.lazychess.chessgame.exception.PlayerAlreadyPartOfGameException;
import com.lazychess.chessgame.exception.PlayerNotPartOfGameException;
import com.lazychess.chessgame.exception.PlayerTwoAlreadyPartOfGameException;
import com.lazychess.chessgame.exception.PlayerTwoHasNotJoinedException;
import com.lazychess.chessgame.exception.WrongPlayerMakingAMoveException;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.json.JsonObjectLatestMoveResponseData;
import com.lazychess.chessgame.json.JsonObjectPlayersResponseData;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.entity.ApplicationUser;
import com.lazychess.chessgame.repository.entity.BoardEntity;
import com.lazychess.chessgame.repository.entity.LatestMoveEntity;
import com.lazychess.chessgame.repository.entity.PlayersEntity;
import com.lazychess.chessgame.repository.mapper.BoardEntityMapper;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardEntityMapper boardEntityMapper;

    public BoardService(BoardRepository boardRepository, BoardEntityMapper boardEntityMapper) {
        this.boardRepository = boardRepository;
        this.boardEntityMapper = boardEntityMapper;
    }

    public JsonObjectBoardResponse createInitialBoardGame(ApplicationUser appUser) {
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(appUser.getId());
        playersEntity.setPlayerOneAppUsername(appUser.getUsername());
        playersEntity.setActivePlayerUsername(playersEntity.getPlayerOneAppUsername());
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(new Board());
        boardEntity.setPlayersEntity(playersEntity);
        boardEntity = boardRepository.saveAndFlush(boardEntity);
        return buildJsonObjectBoardResponse(boardEntity);
    }

    public JsonObjectBoardResponse playerTwoJoinsGame(String boardGameId, ApplicationUser applicationUser) {
        String playersUsername = applicationUser.getUsername();
        String playerTwoId = applicationUser.getId();
        BoardEntity boardEntity = findChessGameById(boardGameId);
        PlayersEntity playersEntity = boardEntity.getPlayersEntity();
        checkIfPlayerAlreadyPartOfThisGame(playersEntity, playersUsername);
        checkIfPlayerTwoIsAlreadyAdded(playersEntity);
        playersEntity.setPlayerTwoAppUsername(playersUsername);
        playersEntity.setPlayerTwoAppUserId(playerTwoId);
        boardEntity.setPlayersEntity(playersEntity);
        boardEntity = boardRepository.saveAndFlush(boardEntity);
        return buildJsonObjectBoardResponse(boardEntity);
    }

    public JsonObjectBoardResponse processChessMove(String boardGameId, String playersUsername, ChessMoveRequest chessMoveRequest) {
        BoardEntity boardEntity = findChessGameById(boardGameId);
        checkIfGameIsInACheckMateState(boardEntity);
        checkIfPlayerTwoHasJoined(boardEntity);
        checkIfPlayerIsPartOfThisGame(boardEntity, playersUsername);
        checkIfItIsSubmittingPlayersTurn(boardEntity, playersUsername);
        checkIfPawnPromotionIsNotPending(boardEntity);
        Board board = boardEntityMapper.fromBoardEntityObject(boardEntity);
        implementMoveOnTheBoard(board, chessMoveRequest);
        BoardEntity updatedBoardEntity = boardEntityMapper.updateBoardEntityObjectAfterMove(board, boardEntity);
        checkIfLastMovePutTheGameInACheckMateState(board, updatedBoardEntity, playersUsername);
        changeActivePlayer(updatedBoardEntity);

        updatedBoardEntity = boardRepository.saveAndFlush(updatedBoardEntity);

        return buildJsonObjectBoardResponse(updatedBoardEntity);
    }

    public JsonObjectBoardResponse processPawnPromotion(String boardGameId, String playersUsername, String promotePawnTo) {
        BoardEntity boardEntity = findChessGameById(boardGameId);
        checkIfGameIsInACheckMateState(boardEntity);
        checkIfPlayerTwoHasJoined(boardEntity);
        checkIfPlayerIsPartOfThisGame(boardEntity, playersUsername);
        checkIfItIsSubmittingPlayersTurn(boardEntity, playersUsername);
        checkIfPawnPromotionIsPending(boardEntity);
        Board board = boardEntityMapper.fromBoardEntityObject(boardEntity);
        board.promoteAPawn(promotePawnTo);
        BoardEntity updatedBoardEntity = boardEntityMapper.updateBoardEntityObjectAfterMove(board, boardEntity);
        checkIfLastMovePutTheGameInACheckMateState(board, updatedBoardEntity, playersUsername);
        changeActivePlayer(updatedBoardEntity);

        updatedBoardEntity = boardRepository.saveAndFlush(updatedBoardEntity);

        return buildJsonObjectBoardResponse(updatedBoardEntity);
    }

    public JsonObjectBoardResponse getBoard(String boardGameId, String username) {
        BoardEntity boardEntity = findChessGameById(boardGameId);
        checkIfPlayerIsPartOfThisGame(boardEntity, username);
        return buildJsonObjectBoardResponse(boardEntity);
    }

    public BoardEntity findChessGameById(String boardGameId) {
        return boardRepository.findById(boardGameId).orElseThrow(() -> new BoardNotFoundException(boardGameId));
    }

    private JsonObjectBoardResponse buildJsonObjectBoardResponse(BoardEntity boardEntity) {
        return JsonObjectBoardResponse.newBuilder()
            .boardId(boardEntity.getId())
            .squares(boardEntity.getSquares())
            .gameState(boardEntity.getStateOfTheGame().toString())
            .currentPlayerColour(boardEntity.getCurrentPlayerColour())
            .players(buildJsonObjectPlayersResponseData(boardEntity.getPlayersEntity()))
            .pawnPromotionPending(boardEntity.isPawnPromotionPending())
            .latestMove(buildJsonObjectLatestMoveResponseData(boardEntity.getLatestMove()))
            .winner(boardEntity.getWinnerUsername())
            .build();
    }

    private JsonObjectLatestMoveResponseData buildJsonObjectLatestMoveResponseData(LatestMoveEntity latestMoveEntity) {
        return JsonObjectLatestMoveResponseData.newBuilder()
            .row(latestMoveEntity.getRow())
            .column(latestMoveEntity.getColumn())
            .build();
    }

    private JsonObjectPlayersResponseData buildJsonObjectPlayersResponseData(PlayersEntity playersEntity) {
        return JsonObjectPlayersResponseData.newBuilder()
            .playerOneAppUserId(playersEntity.getPlayerOneAppUserId())
            .playerOneUsername(playersEntity.getPlayerOneAppUsername())
            .playerTwoAppUserId(playersEntity.getPlayerTwoAppUserId())
            .playerTwoUsername(playersEntity.getPlayerTwoAppUsername())
            .activePlayerUsername(playersEntity.getActivePlayerUsername())
            .build();
    }

    private void checkIfPawnPromotionIsPending(BoardEntity boardEntity) {
        boolean pawnPromotionStatus = boardEntity.isPawnPromotionPending();
        if(!pawnPromotionStatus) {
            throw new PawnPromotionStatusNotPendingException("Cannot promote a pawn at this time");
        }
    }

    private void checkIfPawnPromotionIsNotPending(BoardEntity boardEntity) {
        boolean pawnPromotionStatus = boardEntity.isPawnPromotionPending();
        if(pawnPromotionStatus) {
            throw new PawnPromotionStatusPendingException("Cannot make a move because pawn promotion is pending");
        }
    }

    private void checkIfGameIsInACheckMateState(BoardEntity boardEntity) {
        ChessGameState stateOfTheGame = boardEntity.getStateOfTheGame();
        if(stateOfTheGame != ChessGameState.ONGOING) {
            throw new GameHasFinishedException("The game has finished");
        }
    }

    private void checkIfLastMovePutTheGameInACheckMateState(Board board, BoardEntity updatedBoardEntity, String playersUsername) {
        ChessGameState stateOfTheGame = board.getStateOfTheGame();
        if(stateOfTheGame == ChessGameState.CHECKMATE) {
            updatedBoardEntity.setWinnerUsername(playersUsername);
        } else if (stateOfTheGame == ChessGameState.STALEMATE) {
            updatedBoardEntity.setWinnerUsername("Draw");
        }
    }

    private void checkIfPlayerTwoHasJoined(BoardEntity boardEntity) {
        PlayersEntity playersEntity = boardEntity.getPlayersEntity();
        String playerTwoAppUserId = playersEntity.getPlayerTwoAppUserId();
        if (playerTwoAppUserId == null) {
            throw new PlayerTwoHasNotJoinedException("Cannot make a move until player 2 has joined");
        }
    }

    public void checkIfPlayerIsPartOfThisGame(BoardEntity boardEntity, String playersUsername) {
        PlayersEntity playersEntity = boardEntity.getPlayersEntity();
        String playerOneAppUsername = playersEntity.getPlayerOneAppUsername();
        String playerTwoAppUsername = playersEntity.getPlayerTwoAppUsername();

        if(!(Objects.equals(playerOneAppUsername, playersUsername) || Objects.equals(playerTwoAppUsername, playersUsername))) {
            throw new PlayerNotPartOfGameException("Submitting player is not part of this game");
        }
    }

    private void checkIfItIsSubmittingPlayersTurn(BoardEntity boardEntity, String playersUsername) {
        String activePlayerUsername = boardEntity.getPlayersEntity().getActivePlayerUsername();
        if(!Objects.equals(activePlayerUsername, playersUsername)) {
            throw new WrongPlayerMakingAMoveException("The Player Username does not match active Player Username");
        }
    }

    private void implementMoveOnTheBoard(Board board, ChessMoveRequest chessMoveRequest) {
        int currentRow = chessMoveRequest.currentRow();
        int currentColumn = chessMoveRequest.currentColumn();
        int newRow = chessMoveRequest.newRow();
        int newColumn = chessMoveRequest.newColumn();

        board.movePiece(currentRow, currentColumn, newRow, newColumn);
    }

    private void changeActivePlayer(BoardEntity boardEntity) {
        if(!boardEntity.isPawnPromotionPending()) {
            PlayersEntity playersEntity = boardEntity.getPlayersEntity();
            String playerOneAppUsername = playersEntity.getPlayerOneAppUsername();
            String playerTwoAppUsername = playersEntity.getPlayerTwoAppUsername();
            String activePlayerColour = boardEntity.getCurrentPlayerColour();

            if (Objects.equals(activePlayerColour, BLACK)) {
                playersEntity.setActivePlayerUsername(playerTwoAppUsername);
            } else if (Objects.equals(activePlayerColour, WHITE)) {
                playersEntity.setActivePlayerUsername(playerOneAppUsername);
            }
            boardEntity.setPlayersEntity(playersEntity);
        }
    }

    private void checkIfPlayerAlreadyPartOfThisGame(PlayersEntity playersEntity, String playersUsername) {
        String playerOneAppUsername = playersEntity.getPlayerOneAppUsername();
        String playerTwoAppUsername = playersEntity.getPlayerTwoAppUsername();
        if(Objects.equals(playerOneAppUsername, playersUsername) || Objects.equals(playerTwoAppUsername, playersUsername)) {
            throw new PlayerAlreadyPartOfGameException("Player is already part of the game");
        }
    }

    private void checkIfPlayerTwoIsAlreadyAdded(PlayersEntity playersEntity) {
        String playerTwoAppUsername = playersEntity.getPlayerTwoAppUsername();
        if(playerTwoAppUsername != null) {
            throw new PlayerTwoAlreadyPartOfGameException("Game already has 2 players");
        }
    }
}
