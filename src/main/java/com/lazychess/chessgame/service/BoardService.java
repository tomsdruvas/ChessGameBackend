package com.lazychess.chessgame.service;

import org.springframework.stereotype.Service;

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.json.JsonObjectPlayersResponseData;
import com.lazychess.chessgame.repository.BoardFacade;
import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.repository.entity.PlayersDao;

@Service
public class BoardService {

    private final BoardFacade boardFacade;

    public BoardService(BoardFacade boardFacade) {
        this.boardFacade = boardFacade;
    }

    public JsonObjectBoardResponse createInitialBoardGame(ApplicationUser appUser) {
        BoardDao boardDao = boardFacade.persistCreatedBoard(new Board(), appUser);
        return buildJsonObjectBoardResponse(boardDao);
    }

    public JsonObjectBoardResponse playerTwoJoinsGame(String boardGameId, ApplicationUser applicationUser) {
        BoardDao boardDao = boardFacade.persistPlayerTwoAddedBoard(boardGameId, applicationUser);
        return buildJsonObjectBoardResponse(boardDao);
    }

    public JsonObjectBoardResponse processChessMove(String boardGameId, String playersUsername, ChessMoveDto chessMoveDto) {
        BoardDao boardDao = boardFacade.persistChessMove(boardGameId, playersUsername, chessMoveDto);
        return buildJsonObjectBoardResponse(boardDao);
    }

    public JsonObjectBoardResponse processPawnPromotion(String boardGameId, String playersUsername, String promotePawnTo) {
        BoardDao boardDao = boardFacade.persistUpgradedPawn(boardGameId, playersUsername, promotePawnTo);
        return buildJsonObjectBoardResponse(boardDao);
    }

    public static JsonObjectBoardResponse buildJsonObjectBoardResponse(BoardDao boardDao) {
        return JsonObjectBoardResponse.newBuilder()
            .boardId(boardDao.getId())
            .squares(boardDao.getSquares())
            .gameState(boardDao.getStateOfTheGame().toString())
            .currentPlayerColour(boardDao.getCurrentPlayerColour())
            .players(buildJsonObjectPlayersResponseData(boardDao.getPlayersDao()))
            .winner(boardDao.getWinnerUsername())
            .build();
    }

    public static JsonObjectPlayersResponseData buildJsonObjectPlayersResponseData(PlayersDao playersDao) {
        return JsonObjectPlayersResponseData.newBuilder()
            .playerOneAppUserId(playersDao.getPlayerOneAppUserId())
            .playerOneUsername(playersDao.getPlayerOneAppUsername())
            .playerTwoAppUserId(playersDao.getPlayerTwoAppUserId())
            .playerTwoUsername(playersDao.getPlayerTwoAppUsername())
            .activePlayerUsername(playersDao.getActivePlayerUsername())
            .build();
    }
}
