package com.lazychess.chessgame.service;

import org.springframework.stereotype.Service;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.repository.BoardFacade;
import com.lazychess.chessgame.repository.entity.BoardDao;

@Service
public class BoardService {

    private final BoardFacade boardFacade;

    public BoardService(BoardFacade boardFacade) {
        this.boardFacade = boardFacade;
    }

    public BoardDao createInitialBoardGame(String appUserId) {
        return boardFacade.persistCreatedBoard(new Board(), appUserId);
    }

    public BoardDao playerTwoJoinsGame(String boardGameId, String playerTwoId) {
        return boardFacade.persistPlayerTwoAddedBoard(boardGameId, playerTwoId);
    }

    public BoardDao processChessMove(String boardGameId, String playersId, ChessMoveDto chessMoveDto) {
        return boardFacade.persistChessMove(boardGameId, playersId, chessMoveDto);
    }
}
