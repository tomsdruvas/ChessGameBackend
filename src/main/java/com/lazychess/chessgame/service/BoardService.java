package com.lazychess.chessgame.service;

import org.springframework.stereotype.Service;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.repository.BoardFacade;
import com.lazychess.chessgame.repository.entity.BoardDao;

@Service
public class BoardService {

    private BoardFacade boardFacade;

    public BoardService(BoardFacade boardFacade) {
        this.boardFacade = boardFacade;
    }

    public BoardDao createInitialBoardGame() {
        return boardFacade.persistBoard(new Board());
    }
}
