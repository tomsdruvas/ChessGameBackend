package com.lazychess.chessgame.repository.mapper;

import org.springframework.stereotype.Component;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.LegalMoveSquare;
import com.lazychess.chessgame.repository.entity.BoardEntity;
import com.lazychess.chessgame.repository.entity.LatestMoveEntity;

@Component
public class BoardEntityMapper {

    public BoardEntity fromBoardObject(Board board) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setSquares(board.getSquares());
        boardEntity.setStateOfTheGame(board.getStateOfTheGame());
        boardEntity.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardEntity.setPawnPromotionPending(board.isPawnPromotionPending());
        boardEntity.setLatestMove(fromLegalSquare(board.getLatestMove()));
        return boardEntity;
    }

    public BoardEntity updateBoardEntityObjectAfterMove(Board board, BoardEntity boardEntity) {
        boardEntity.setSquares(board.getSquares());
        boardEntity.setStateOfTheGame(board.getStateOfTheGame());
        boardEntity.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardEntity.setPawnPromotionPending(board.isPawnPromotionPending());
        boardEntity.setLatestMove(fromLegalSquare(board.getLatestMove()));
        return boardEntity;
    }

    public Board fromBoardEntityObject(BoardEntity boardEntity) {
        Board board = new Board();
        board.setSquares(boardEntity.getSquares());
        board.setStateOfTheGame(boardEntity.getStateOfTheGame());
        board.setCurrentPlayerColourState(boardEntity.getCurrentPlayerColour());
        board.setPawnPromotionPending(boardEntity.isPawnPromotionPending());
        board.setLatestMove(fromLatestMoveEntity(boardEntity.getLatestMove()));
        return board;
    }

    private LatestMoveEntity fromLegalSquare(LegalMoveSquare legalMoveSquare) {
        LatestMoveEntity latestMoveEntity = new LatestMoveEntity();
        latestMoveEntity.setRow(legalMoveSquare.getRow());
        latestMoveEntity.setColumn(legalMoveSquare.getColumn());
        return latestMoveEntity;
    }

    private LegalMoveSquare fromLatestMoveEntity(LatestMoveEntity latestMoveEntity) {
        int row = latestMoveEntity.getRow();
        int column = latestMoveEntity.getColumn();
        return new LegalMoveSquare(row, column);
    }
}
