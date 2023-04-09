package com.lazychess.chessgame.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.exception.BoardNotFoundException;
import com.lazychess.chessgame.exception.GameHasFinishedException;
import com.lazychess.chessgame.exception.PawnPromotionStatusNotPendingException;
import com.lazychess.chessgame.exception.PlayerAlreadyPartOfGameException;
import com.lazychess.chessgame.exception.PlayerNotPartOfGameException;
import com.lazychess.chessgame.exception.PlayerTwoHasNotJoinedException;
import com.lazychess.chessgame.exception.WrongPlayerMakingAMoveException;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.repository.entity.PlayersDao;
import com.lazychess.chessgame.repository.mapper.BoardDaoMapper;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    private static final String CHESS_BOARD_ID = "test_chess_game_id";
    private static final String TEST_USER_ONE_ID = "test_user_one_id";
    private static final String TEST_USER_ONE = "test_user_one";
    private static final String TEST_USER_TWO_ID = "test_user_two_id";
    private static final String TEST_USER_TWO = "test_user_two";
    
    @Mock
    private BoardRepository boardRepository;
    private BoardDaoMapper boardDaoMapper;
    private Board initialBoard;

    private BoardService underTest;

    @BeforeEach
    void setUp() {
        boardDaoMapper = new BoardDaoMapper();
        underTest = new BoardService(boardRepository, boardDaoMapper);
        initialBoard = new Board();
    }

    @Test
    void createInitialBoardGame_userShouldBeSetAsPlayerOneAndActivePlayer_andShouldReturnJsonResponseOfChessBoard() {
        Board initialBoard = new Board();
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        boardDao.setPlayersDao(playersDao);

        ApplicationUser appUser = new ApplicationUser(TEST_USER_ONE_ID, TEST_USER_ONE, "test_password");

        ArgumentCaptor<BoardDao> boardDaoCaptor = ArgumentCaptor.forClass(BoardDao.class);

        when(boardRepository.saveAndFlush(any(BoardDao.class))).thenReturn(boardDao);

        JsonObjectBoardResponse initialBoardGameJsonResponse = underTest.createInitialBoardGame(appUser);

        assertBoardJsonResponse(initialBoardGameJsonResponse);

        verify(boardRepository).saveAndFlush(boardDaoCaptor.capture());
        PlayersDao savedCurrentPlayerDao = boardDaoCaptor.getValue().getPlayersDao();
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayerDao.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
    }

    @Test
    void whenPlayerTwoTriesToJoinGame_userShouldBeSetAsPlayerTwo_andShouldReturnJsonResponseOfChessBoard() {
        Board initialBoard = new Board();
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);

        ApplicationUser appUser = new ApplicationUser(TEST_USER_TWO_ID, TEST_USER_TWO, "test_password");

        ArgumentCaptor<BoardDao> boardDaoCaptor = ArgumentCaptor.forClass(BoardDao.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));
        when(boardRepository.saveAndFlush(any(BoardDao.class))).thenReturn(boardDao);

        JsonObjectBoardResponse initialBoardGameJsonResponse = underTest.playerTwoJoinsGame(CHESS_BOARD_ID, appUser);

        assertBoardJsonResponse(initialBoardGameJsonResponse);

        verify(boardRepository).saveAndFlush(boardDaoCaptor.capture());
        PlayersDao savedCurrentPlayerDao = boardDaoCaptor.getValue().getPlayersDao();
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayerDao.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
    }

    @Test
    void whenPlayerTwoTriesToJoinGame_andPlayerIsAlreadyPartOfTheGame_andItIsThePlayerOneJoining_throwException() {
        BoardDao boardDao = boardDaoMapper.fromBoardObject(new Board());
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);

        ApplicationUser appUser = new ApplicationUser(TEST_USER_ONE_ID, TEST_USER_ONE, "test_password");
        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));

        assertThatThrownBy(() -> underTest.playerTwoJoinsGame(CHESS_BOARD_ID, appUser)).isExactlyInstanceOf(PlayerAlreadyPartOfGameException.class)
            .hasMessage("Player is already part of the game");
    }

    @Test
    void whenPlayerTwoTriesToJoinGame_andInvalidChessBoardIdIsGiven_throwException() {
        ApplicationUser appUser = new ApplicationUser(TEST_USER_ONE_ID, TEST_USER_ONE, "test_password");
        when(boardRepository.findById("invalid_chess_board_id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.playerTwoJoinsGame("invalid_chess_board_id", appUser))
            .isExactlyInstanceOf(BoardNotFoundException.class)
            .hasMessage("Board with ID: invalid_chess_board_id does not exist");
    }

    @Test
    void whenPlayerOneMakesAMove_moveShouldBeProcessedOnTheBoard() {
        Board initialBoard = new Board();
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);
        ChessMoveDto chessMoveDto = new ChessMoveDto(6, 0, 5, 0);

        ArgumentCaptor<BoardDao> boardDaoCaptor = ArgumentCaptor.forClass(BoardDao.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));
        when(boardRepository.saveAndFlush(any(BoardDao.class))).thenReturn(boardDao);

        JsonObjectBoardResponse boardGameJsonResponse = underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveDto);

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("black");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(boardGameJsonResponse.getWinner()).isBlank();

        verify(boardRepository).saveAndFlush(boardDaoCaptor.capture());
        PlayersDao savedCurrentPlayerDao = boardDaoCaptor.getValue().getPlayersDao();
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayerDao.getActivePlayerUsername()).isEqualTo(TEST_USER_TWO);
    }

    @Test
    void whenPlayerOneMakesAMove_andGameIsInCheckmateState_ShouldThrowException() {
        Board initialBoard = new Board();
        initialBoard.setStateOfTheGame(ChessGameState.CHECKMATE);
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);
        ChessMoveDto chessMoveDto = new ChessMoveDto(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveDto))
            .isExactlyInstanceOf(GameHasFinishedException.class)
            .hasMessage("The game has finished");
    }

    @Test
    void whenPlayerOneMakesAMove_andPlayerTwoHasNotJoined_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);
        ChessMoveDto chessMoveDto = new ChessMoveDto(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveDto))
            .isExactlyInstanceOf(PlayerTwoHasNotJoinedException.class)
            .hasMessage("Cannot make a move until player 2 has joined");
    }

    @Test
    void whenPlayerNotPartOfTheGameMakesAMove_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);
        ChessMoveDto chessMoveDto = new ChessMoveDto(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE + "1", chessMoveDto))
            .isExactlyInstanceOf(PlayerNotPartOfGameException.class)
            .hasMessage("Submitting player is not part of this game");
    }

    @Test
    void whenPlayerTwoMakesAMove_andItIsNotTheirTurn_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);
        ChessMoveDto chessMoveDto = new ChessMoveDto(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_TWO, chessMoveDto))
            .isExactlyInstanceOf(WrongPlayerMakingAMoveException.class)
            .hasMessage("The Player Username does not match active Player Username");
    }

    @Test
    void whenPlayerOneMakesAPawnPromotionCall_andPawnPromotionIsNotPending_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));

        assertThatThrownBy(() -> underTest.processPawnPromotion(CHESS_BOARD_ID, TEST_USER_ONE, "Queen"))
            .isExactlyInstanceOf(PawnPromotionStatusNotPendingException.class)
            .hasMessage("Cannot promote a pawn at this time");
    }

    @Test
    void whenPlayerOneMakesAMove_andPawnPromotionIsPending() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 0, 2, 2),
            new ChessMoveDto(0, 0, 2, 3),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board initialBoard = new Board(preInitChessMoveDtoList);

        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);

        ChessMoveDto chessMoveDto = new ChessMoveDto(1, 0, 0, 0);

        ArgumentCaptor<BoardDao> boardDaoCaptor = ArgumentCaptor.forClass(BoardDao.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));
        when(boardRepository.saveAndFlush(any(BoardDao.class))).thenReturn(boardDao);

        JsonObjectBoardResponse boardGameJsonResponse = underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveDto);

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("white");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isTrue();
        assertThat(boardGameJsonResponse.getWinner()).isBlank();

        verify(boardRepository).saveAndFlush(boardDaoCaptor.capture());
        PlayersDao savedCurrentPlayerDao = boardDaoCaptor.getValue().getPlayersDao();
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayerDao.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
    }

    @Test
    void whenPlayerOneMakesAPawnPromotionCall_andPawnPromotionIsPending_ShouldPromotePawn() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(1, 0, 2, 2),
            new ChessMoveDto(0, 0, 2, 3),
            new ChessMoveDto(6, 0, 1, 0)
        );
        Board initialBoard = new Board(preInitChessMoveDtoList);
        initialBoard.movePiece(1,0,0,0);

        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);

        ArgumentCaptor<BoardDao> boardDaoCaptor = ArgumentCaptor.forClass(BoardDao.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));
        when(boardRepository.saveAndFlush(any(BoardDao.class))).thenReturn(boardDao);

        JsonObjectBoardResponse boardGameJsonResponse = underTest.processPawnPromotion(CHESS_BOARD_ID, TEST_USER_ONE, "Queen");

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("black");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(boardGameJsonResponse.getWinner()).isBlank();

        verify(boardRepository).saveAndFlush(boardDaoCaptor.capture());
        PlayersDao savedCurrentPlayerDao = boardDaoCaptor.getValue().getPlayersDao();
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayerDao.getActivePlayerUsername()).isEqualTo(TEST_USER_TWO);
    }

    @Test
    void whenPlayerOneMakesAMove_andPlayerTwoMakesMove_gameShouldEndInCheckmate() {
        List<ChessMoveDto> preInitChessMoveDtoList = List.of(
            new ChessMoveDto(0, 4, 5, 2),
            new ChessMoveDto(6, 2, 2, 3),
            new ChessMoveDto(6, 0, 2, 7)
        );
        Board initialBoard = new Board(preInitChessMoveDtoList);

        BoardDao boardDao = boardDaoMapper.fromBoardObject(initialBoard);
        boardDao.setId(CHESS_BOARD_ID);
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersDao.setPlayerOneAppUsername(TEST_USER_ONE);
        playersDao.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersDao.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersDao.setActivePlayerUsername(TEST_USER_ONE);
        boardDao.setPlayersDao(playersDao);

        ChessMoveDto chessMoveDtoWhite = new ChessMoveDto(6, 7, 5, 7);
        ChessMoveDto chessMoveDtoBlack = new ChessMoveDto(5, 2, 5, 1);


        ArgumentCaptor<BoardDao> boardDaoCaptor = ArgumentCaptor.forClass(BoardDao.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardDao));
        when(boardRepository.saveAndFlush(any(BoardDao.class))).thenReturn(boardDao);

        underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveDtoWhite);
        JsonObjectBoardResponse boardGameJsonResponse = underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_TWO, chessMoveDtoBlack);

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("CHECKMATE");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("white");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(boardGameJsonResponse.getWinner()).isEqualTo(TEST_USER_TWO);

        verify(boardRepository, times(2)).saveAndFlush(boardDaoCaptor.capture());
        PlayersDao savedCurrentPlayerDao = boardDaoCaptor.getValue().getPlayersDao();
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayerDao.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayerDao.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayerDao.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
    }

    private void assertBoardJsonResponse(JsonObjectBoardResponse boardGameJsonResponse) {
        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("white");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(boardGameJsonResponse.getWinner()).isBlank();
    }


}
