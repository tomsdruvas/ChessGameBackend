package com.lazychess.chessgame.repository;

import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.exception.BoardNotFoundException;
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
    public BoardDao persistCreatedBoard(Board board, ApplicationUser applicationUser) {
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(applicationUser.getId());
        playersDao.setPlayerOneAppUsername(applicationUser.getUsername());
        playersDao.setActivePlayerUsername(playersDao.getPlayerOneAppUsername());
        BoardDao boardDao = boardDaoMapper.fromBoardObject(board);
        boardDao.setPlayersDao(playersDao);
        boardDao = boardRepository.saveAndFlush(boardDao);
        return boardDao;
    }

    @Transactional
    public BoardDao persistPlayerTwoAddedBoard(String boardGameId, ApplicationUser applicationUser) {
        String playersUsername = applicationUser.getUsername();
        String playerTwoId = applicationUser.getId();
        BoardDao boardDao = boardRepository.findById(boardGameId).orElseThrow(() -> new BoardNotFoundException("This board does not exist"));
        PlayersDao playersDao = boardDao.getPlayersDao();
        checkIfPlayerAlreadyPartOfThisGame(playersDao, playersUsername);
        playersDao.setPlayerTwoAppUsername(playersUsername);
        playersDao.setPlayerTwoAppUserId(playerTwoId);
        boardDao.setPlayersDao(playersDao);
        boardDao = boardRepository.saveAndFlush(boardDao);
        return boardDao;
    }

    @Transactional
    public BoardDao persistChessMove(String boardGameId, String playersUsername, ChessMoveDto chessMoveDto) {
        BoardDao boardDao = boardRepository.findById(boardGameId).orElseThrow(() -> new BoardNotFoundException("This board does not exist"));
        checkIfGameIsInACheckMateState(boardDao);
        checkIfPlayerTwoHasJoined(boardDao);
        checkIfPlayerIsPartOfThisGame(boardDao, playersUsername);
        checkIfItIsSubmittingPlayersTurn(boardDao, playersUsername);
        Board board = boardDaoMapper.fromBoardDaoObject(boardDao);
        implementMoveOnTheBoard(board, chessMoveDto);
        BoardDao updatedBoardDao = boardDaoMapper.updateBoardDaoObjectAfterMove(board, boardDao);
        checkIfLastMovePutTheGameInACheckMateState(board, updatedBoardDao, playersUsername);
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

    private void checkIfLastMovePutTheGameInACheckMateState(Board board, BoardDao updatedBoardDao, String playersUsername) {
        ChessGameState stateOfTheGame = board.getStateOfTheGame();
        if(stateOfTheGame == ChessGameState.CHECKMATE) {
            updatedBoardDao.setWinnerUsername(playersUsername);
        } else if (stateOfTheGame == ChessGameState.STALEMATE) {
            updatedBoardDao.setWinnerUsername("Draw");
        }
    }

    private void checkIfPlayerTwoHasJoined(BoardDao boardDao) {
        PlayersDao playersDao = boardDao.getPlayersDao();
        String playerTwoAppUserId = playersDao.getPlayerTwoAppUserId();
        if (playerTwoAppUserId == null) {
            throw new PlayerTwoHasNotJoinedException("Cannot make a move until player 2 has joined");
        }
    }

    private void checkIfPlayerIsPartOfThisGame(BoardDao boardDao, String playersUsername) {
        PlayersDao playersDao = boardDao.getPlayersDao();
        String playerOneAppUsername = playersDao.getPlayerOneAppUsername();
        String playerTwoAppUsername = playersDao.getPlayerTwoAppUsername();

        if(!(Objects.equals(playerOneAppUsername, playersUsername) || Objects.equals(playerTwoAppUsername, playersUsername))) {
            throw new PlayerNotPartOfGameException("Submitting player is not part of this game");
        }
    }

    private void checkIfItIsSubmittingPlayersTurn(BoardDao boardDao, String playersUsername) {
        String activePlayerUsername = boardDao.getPlayersDao().getActivePlayerUsername();
        if(!Objects.equals(activePlayerUsername, playersUsername)) {
            throw new WrongPlayerMakingAMoveException("The Player Username does not match active Player Username");
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
        String playerOneAppUsername = playersDao.getPlayerOneAppUsername();
        String playerTwoAppUsername= playersDao.getPlayerTwoAppUsername();
        String activePlayerUsername = playersDao.getActivePlayerUsername();

        if(Objects.equals(activePlayerUsername, playerOneAppUsername)) {
            playersDao.setActivePlayerUsername(playerTwoAppUsername);
        }
        else if (Objects.equals(activePlayerUsername, playerTwoAppUsername)) {
            playersDao.setActivePlayerUsername(playerOneAppUsername);

        }
        boardDao.setPlayersDao(playersDao);
    }

    private void checkIfPlayerAlreadyPartOfThisGame(PlayersDao playersDao, String playersUsername) {
        String playerOneAppUsername = playersDao.getPlayerOneAppUsername();
        String playerTwoAppUsername = playersDao.getPlayerTwoAppUsername();
        if(Objects.equals(playerOneAppUsername, playersUsername) || Objects.equals(playerTwoAppUsername, playersUsername)) {
            throw new PlayerAlreadyPartOfGameException("Player is already part of the game");
        }
    }
}
