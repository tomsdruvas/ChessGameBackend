package com.lazychess.chessgame.repository.mapper;

import org.springframework.stereotype.Component;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.repository.entity.BoardDao;

@Component
public class BoardDaoMapper {

    public BoardDao fromBoardObject(Board board) {
        BoardDao boardDao = new BoardDao();
        boardDao.setSquares(board.getSquares());
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        return boardDao;
    }

    public BoardDao updateBoardDaoObjectAfterMove(Board board, BoardDao boardDao) {
        boardDao.setSquares(board.getSquares());
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        return boardDao;
    }

    public Board fromBoardDaoObject(BoardDao boardDao) {
        Board board = new Board();
        board.setSquares(boardDao.getSquares());
        board.setStateOfTheGame(boardDao.getStateOfTheGame());
        board.setCurrentPlayerColourState(boardDao.getCurrentPlayerColour());
        board.setPawnPromotionPending(boardDao.isPawnPromotionPending());
        return board;
    }
}
