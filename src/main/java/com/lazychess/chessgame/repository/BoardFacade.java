package com.lazychess.chessgame.repository;

import org.springframework.transaction.annotation.Transactional;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.repository.entity.PlayersDao;
import com.lazychess.chessgame.repository.mapper.BoardDaoMapper;

public class BoardFacade {
    private final BoardRepository boardRepository;
    private final BoardDaoMapper boardDaoMapper;

    public BoardFacade(BoardRepository boardRepository, BoardDaoMapper boardDaoMapper) {
        this.boardRepository = boardRepository;
        this.boardDaoMapper = boardDaoMapper;
    }

    @Transactional
    public BoardDao persistCreatedBoard(Board board, String appUserId) {
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(appUserId);
        playersDao.setActivePlayer(appUserId);
        BoardDao boardDao = boardDaoMapper.fromBoardObject(board);
        boardDao.setPlayersDao(playersDao);
        boardDao = boardRepository.saveAndFlush(boardDao);
        return boardDao;
    }

    @Transactional
    public BoardDao persistPlayerTwoAddedBoard(String boardGameId, String playerTwoId) {
        BoardDao boardDao = boardRepository.findById(boardGameId).orElseThrow(() -> new RuntimeException("This board does not exist"));
        PlayersDao playersDao = boardDao.getPlayersDao();
        playersDao.setPlayerTwoAppUserId(playerTwoId);
        boardDao.setPlayersDao(playersDao);
        boardDao = boardRepository.saveAndFlush(boardDao);
        return boardDao;
    }
}
