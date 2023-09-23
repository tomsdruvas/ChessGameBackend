package com.lazychess.chessgame.repository.mapper;

import org.springframework.stereotype.Component;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.repository.entity.LatestMoveDao;

@Component
public class BoardDaoMapper {

    public BoardDao fromBoardObject(Board board) {
        BoardDao boardDao = new BoardDao();
        boardDao.setSquares(board.getSquares());
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        boardDao.setLatestMove(fromLegalSquare(board.getLatestMove()));
        return boardDao;
    }

    public BoardDao updateBoardDaoObjectAfterMove(Board board, BoardDao boardDao) {
        boardDao.setSquares(board.getSquares());
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPawnPromotionPending(board.isPawnPromotionPending());
        boardDao.setLatestMove(fromLegalSquare(board.getLatestMove()));
        return boardDao;
    }

    public Board fromBoardDaoObject(BoardDao boardDao) {
        Board board = new Board();
        board.setSquares(boardDao.getSquares());
        board.setStateOfTheGame(boardDao.getStateOfTheGame());
        board.setCurrentPlayerColourState(boardDao.getCurrentPlayerColour());
        board.setPawnPromotionPending(boardDao.isPawnPromotionPending());
        board.setLatestMove(fromLatestMoveDao(boardDao.getLatestMove()));
        return board;
    }

    private LatestMoveDao fromLegalSquare(LegalMoveSquare legalMoveSquare) {
        LatestMoveDao latestMoveDao = new LatestMoveDao();
        latestMoveDao.setRow(legalMoveSquare.getRow());
        latestMoveDao.setColumn(legalMoveSquare.getColumn());
        return latestMoveDao;
    }

    private LegalMoveSquare fromLatestMoveDao(LatestMoveDao latestMoveDao) {
        int row = latestMoveDao.getRow();
        int column = latestMoveDao.getColumn();
        return new LegalMoveSquare(row, column);
    }
}
