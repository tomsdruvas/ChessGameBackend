package com.lazychess.chessgame.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.exception.BoardNotFoundException;
import com.lazychess.chessgame.exception.PlayerAlreadyPartOfGameException;
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

    private void assertBoardJsonResponse(JsonObjectBoardResponse initialBoardGameJsonResponse) {
        assertThat(initialBoardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(initialBoardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(initialBoardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(initialBoardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("white");
        assertThat(initialBoardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(initialBoardGameJsonResponse.getWinner()).isBlank();
    }


}
