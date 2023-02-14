package com.lazychess.chessgame.service;

import org.springframework.stereotype.Service;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.repository.BoardFacade;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.entity.BoardDao;

@Service
public class BoardService {

    private final BoardFacade boardFacade;
    private final BoardRepository boardRepository;

    public BoardService(BoardFacade boardFacade, BoardRepository boardRepository) {
        this.boardFacade = boardFacade;
        this.boardRepository = boardRepository;
    }

    public BoardDao createInitialBoardGame(String appUserId) {
        return boardFacade.persistCreatedBoard(new Board(), appUserId);
    }

    public BoardDao playerTwoJoinsGame(String boardGameId, String playerTwoId) {
        return boardFacade.persistPlayerTwoAddedBoard(boardGameId, playerTwoId);

    }
}
