package com.lazychess.chessgame.repository;

import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.exception.GameHasFinishedException;
import com.lazychess.chessgame.exception.PlayerAlreadyPartOfGameException;
import com.lazychess.chessgame.exception.PlayerNotPartOfGameException;
import com.lazychess.chessgame.exception.PlayerTwoHasNotJoinedException;
import com.lazychess.chessgame.exception.WrongPlayerMakingAMoveException;
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
        checkIfPlayerAlreadyPartOfThisGame(playersDao, playerTwoId);
        playersDao.setPlayerTwoAppUserId(playerTwoId);
        boardDao.setPlayersDao(playersDao);
        boardDao = boardRepository.saveAndFlush(boardDao);
        return boardDao;
    }

    @Transactional
    public BoardDao persistChessMove(String boardGameId, String playersId, ChessMoveDto chessMoveDto) {
        BoardDao boardDao = boardRepository.findById(boardGameId).orElseThrow(() -> new RuntimeException("This board does not exist"));
        checkIfGameIsInACheckMateState(boardDao);
        checkIfPlayerTwoHasJoined(boardDao);
        checkIfPlayerIsPartOfThisGame(boardDao, playersId);
        checkIfItIsSubmittingPlayersTurn(boardDao, playersId);
        Board board = boardDaoMapper.fromBoardDaoObject(boardDao);
        implementMoveOnTheBoard(board, chessMoveDto);
        BoardDao updatedBoardDao = boardDaoMapper.updateBoardDaoObjectAfterMove(board, boardDao);
        checkIfLastMovePutTheGameInACheckMateState(board, updatedBoardDao, playersId);
        changeActivePlayer(updatedBoardDao);

        updatedBoardDao = boardRepository.saveAndFlush(updatedBoardDao);
        return updatedBoardDao;

    }

    private void checkIfGameIsInACheckMateState(BoardDao boardDao) {
        ChessGameState stateOfTheGame = boardDao.getStateOfTheGame();
        if(stateOfTheGame != ChessGameState.ONGOING) {
            throw new GameHasFinishedException("The game has finished");
        }
    }

    private void checkIfLastMovePutTheGameInACheckMateState(Board board, BoardDao updatedBoardDao, String playersId) {
        ChessGameState stateOfTheGame = board.getStateOfTheGame();
        if(stateOfTheGame == ChessGameState.CHECKMATE) {
            updatedBoardDao.setWinnerUserId(playersId);
        } else if (stateOfTheGame == ChessGameState.STALEMATE) {
            updatedBoardDao.setWinnerUserId("Draw");
        }
    }

    private void checkIfPlayerTwoHasJoined(BoardDao boardDao) {
        PlayersDao playersDao = boardDao.getPlayersDao();
        String playerTwoAppUserId = playersDao.getPlayerTwoAppUserId();
        if (playerTwoAppUserId == null) {
            throw new PlayerTwoHasNotJoinedException("Cannot make a move until player 2 has joined");
        }
    }

    private void checkIfPlayerIsPartOfThisGame(BoardDao boardDao, String playersId) {
        PlayersDao playersDao = boardDao.getPlayersDao();
        String playerOneAppUserId = playersDao.getPlayerOneAppUserId();
        String playerTwoAppUserId = playersDao.getPlayerTwoAppUserId();

        if(!(Objects.equals(playerOneAppUserId, playersId) || Objects.equals(playerTwoAppUserId, playersId))) {
            throw new PlayerNotPartOfGameException("Submitting player is not part of this game");
        }
    }

    private void checkIfItIsSubmittingPlayersTurn(BoardDao boardDao, String playersId) {
        String activePlayerId = boardDao.getPlayersDao().getActivePlayer();
        if(!Objects.equals(activePlayerId, playersId)) {
            throw new WrongPlayerMakingAMoveException("The Player ID does not match active Player ID");
        }
    }

    private void implementMoveOnTheBoard(Board board, ChessMoveDto chessMoveDto) {
        int currentRow = chessMoveDto.currentRow();
        int currentColumn = chessMoveDto.currentColumn();
        int newRow = chessMoveDto.newRow();
        int newColumn = chessMoveDto.newColumn();
        board.movePiece(currentRow, currentColumn, newRow, newColumn);
    }

    private void changeActivePlayer(BoardDao boardDao) {
        PlayersDao playersDao = boardDao.getPlayersDao();
        String playerOneAppUserId = playersDao.getPlayerOneAppUserId();
        String playerTwoAppUserId = playersDao.getPlayerTwoAppUserId();
        String activePlayerId = playersDao.getActivePlayer();

        if(Objects.equals(activePlayerId, playerOneAppUserId)) {
            playersDao.setActivePlayer(playerTwoAppUserId);
        }
        else if (Objects.equals(activePlayerId, playerTwoAppUserId)) {
            playersDao.setActivePlayer(playerOneAppUserId);

        }
        boardDao.setPlayersDao(playersDao);
    }

    private void checkIfPlayerAlreadyPartOfThisGame(PlayersDao playersDao, String playerTwoId) {
        String playerOneAppUserId = playersDao.getPlayerOneAppUserId();
        String playerTwoAppUserId = playersDao.getPlayerTwoAppUserId();
        if(Objects.equals(playerOneAppUserId, playerTwoId) || Objects.equals(playerTwoAppUserId, playerTwoId)) {
            throw new PlayerAlreadyPartOfGameException("Player is already part of the game");
        }
    }
}
