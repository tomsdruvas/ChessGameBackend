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

import com.lazychess.chessgame.repository.entity.ApplicationUser;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.chessgame.ChessGameState;
import com.lazychess.chessgame.dto.ChessMoveRequest;
import com.lazychess.chessgame.exception.BoardNotFoundException;
import com.lazychess.chessgame.exception.GameHasFinishedException;
import com.lazychess.chessgame.exception.PawnPromotionStatusNotPendingException;
import com.lazychess.chessgame.exception.PlayerAlreadyPartOfGameException;
import com.lazychess.chessgame.exception.PlayerNotPartOfGameException;
import com.lazychess.chessgame.exception.PlayerTwoHasNotJoinedException;
import com.lazychess.chessgame.exception.WrongPlayerMakingAMoveException;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.entity.BoardEntity;
import com.lazychess.chessgame.repository.entity.PlayersEntity;
import com.lazychess.chessgame.repository.mapper.BoardEntityMapper;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    private static final String CHESS_BOARD_ID = "test_chess_game_id";
    private static final String TEST_USER_ONE_ID = "test_user_one_id";
    private static final String TEST_USER_ONE = "test_user_one";
    private static final String TEST_USER_TWO_ID = "test_user_two_id";
    private static final String TEST_USER_TWO = "test_user_two";
    
    @Mock
    private BoardRepository boardRepository;
    private BoardEntityMapper boardEntityMapper;
    private Board initialBoard;

    private BoardService underTest;

    @BeforeEach
    void setUp() {
        boardEntityMapper = new BoardEntityMapper();
        underTest = new BoardService(boardRepository, boardEntityMapper);
        initialBoard = new Board();
    }

    @Test
    void createInitialBoardGame_userShouldBeSetAsPlayerOneAndActivePlayer_andShouldReturnJsonResponseOfChessBoard() {
        Board initialBoard = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        boardEntity.setPlayersEntity(playersEntity);

        ApplicationUser appUser = new ApplicationUser(TEST_USER_ONE_ID, TEST_USER_ONE, "test_password");

        ArgumentCaptor<BoardEntity> boardCaptor = ArgumentCaptor.forClass(BoardEntity.class);

        when(boardRepository.saveAndFlush(any(BoardEntity.class))).thenReturn(boardEntity);

        JsonObjectBoardResponse initialBoardGameJsonResponse = underTest.createInitialBoardGame(appUser);

        assertBoardJsonResponse(initialBoardGameJsonResponse);

        verify(boardRepository).saveAndFlush(boardCaptor.capture());
        PlayersEntity savedCurrentPlayer = boardCaptor.getValue().getPlayersEntity();
        assertThat(savedCurrentPlayer.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayer.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayer.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
    }

    @Test
    void whenPlayerTwoTriesToJoinGame_userShouldBeSetAsPlayerTwo_andShouldReturnJsonResponseOfChessBoard() {
        Board initialBoard = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);

        ApplicationUser appUser = new ApplicationUser(TEST_USER_TWO_ID, TEST_USER_TWO, "test_password");

        ArgumentCaptor<BoardEntity> boardEntityCaptor = ArgumentCaptor.forClass(BoardEntity.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));
        when(boardRepository.saveAndFlush(any(BoardEntity.class))).thenReturn(boardEntity);

        JsonObjectBoardResponse initialBoardGameJsonResponse = underTest.playerTwoJoinsGame(CHESS_BOARD_ID, appUser);

        assertBoardJsonResponse(initialBoardGameJsonResponse);

        verify(boardRepository).saveAndFlush(boardEntityCaptor.capture());
        PlayersEntity savedCurrentPlayer = boardEntityCaptor.getValue().getPlayersEntity();
        assertThat(savedCurrentPlayer.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayer.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayer.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
    }

    @Test
    void whenPlayerTwoTriesToJoinGame_andPlayerIsAlreadyPartOfTheGame_andItIsThePlayerOneJoining_throwException() {
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(new Board());
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);

        ApplicationUser appUser = new ApplicationUser(TEST_USER_ONE_ID, TEST_USER_ONE, "test_password");
        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));

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
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);
        ChessMoveRequest chessMoveRequest = new ChessMoveRequest(6, 0, 5, 0);

        ArgumentCaptor<BoardEntity> boardCaptor = ArgumentCaptor.forClass(BoardEntity.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));
        when(boardRepository.saveAndFlush(any(BoardEntity.class))).thenReturn(boardEntity);

        JsonObjectBoardResponse boardGameJsonResponse = underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveRequest);

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("black");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(boardGameJsonResponse.getWinner()).isBlank();

        verify(boardRepository).saveAndFlush(boardCaptor.capture());
        PlayersEntity savedCurrentPlayer = boardCaptor.getValue().getPlayersEntity();
        assertThat(savedCurrentPlayer.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayer.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayer.getActivePlayerUsername()).isEqualTo(TEST_USER_TWO);
    }

    @Test
    void whenPlayerOneMakesAMove_andGameIsInCheckmateState_ShouldThrowException() {
        Board initialBoard = new Board();
        initialBoard.setStateOfTheGame(ChessGameState.CHECKMATE);
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);
        ChessMoveRequest chessMoveRequest = new ChessMoveRequest(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveRequest))
            .isExactlyInstanceOf(GameHasFinishedException.class)
            .hasMessage("The game has finished");
    }

    @Test
    void whenPlayerOneMakesAMove_andPlayerTwoHasNotJoined_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);
        ChessMoveRequest chessMoveRequest = new ChessMoveRequest(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveRequest))
            .isExactlyInstanceOf(PlayerTwoHasNotJoinedException.class)
            .hasMessage("Cannot make a move until player 2 has joined");
    }

    @Test
    void whenPlayerNotPartOfTheGameMakesAMove_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);
        ChessMoveRequest chessMoveRequest = new ChessMoveRequest(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE + "1", chessMoveRequest))
            .isExactlyInstanceOf(PlayerNotPartOfGameException.class)
            .hasMessage("Submitting player is not part of this game");
    }

    @Test
    void whenPlayerTwoMakesAMove_andItIsNotTheirTurn_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);
        ChessMoveRequest chessMoveRequest = new ChessMoveRequest(6, 0, 5, 0);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));

        assertThatThrownBy(() -> underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_TWO, chessMoveRequest))
            .isExactlyInstanceOf(WrongPlayerMakingAMoveException.class)
            .hasMessage("The Player Username does not match active Player Username");
    }

    @Test
    void whenPlayerOneMakesAPawnPromotionCall_andPawnPromotionIsNotPending_ShouldThrowException() {
        Board initialBoard = new Board();
        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));

        assertThatThrownBy(() -> underTest.processPawnPromotion(CHESS_BOARD_ID, TEST_USER_ONE, "Queen"))
            .isExactlyInstanceOf(PawnPromotionStatusNotPendingException.class)
            .hasMessage("Cannot promote a pawn at this time");
    }

    @Test
    void whenPlayerOneMakesAMove_andPawnPromotionIsPending() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(1, 0, 2, 2),
            new ChessMoveRequest(0, 0, 2, 3),
            new ChessMoveRequest(6, 0, 1, 0)
        );
        Board initialBoard = new Board(preInitChessMoveRequestList);

        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);

        ChessMoveRequest chessMoveRequest = new ChessMoveRequest(1, 0, 0, 0);

        ArgumentCaptor<BoardEntity> boardCaptor = ArgumentCaptor.forClass(BoardEntity.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));
        when(boardRepository.saveAndFlush(any(BoardEntity.class))).thenReturn(boardEntity);

        JsonObjectBoardResponse boardGameJsonResponse = underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveRequest);

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("white");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isTrue();
        assertThat(boardGameJsonResponse.getWinner()).isBlank();

        verify(boardRepository).saveAndFlush(boardCaptor.capture());
        PlayersEntity savedCurrentPlayer = boardCaptor.getValue().getPlayersEntity();
        assertThat(savedCurrentPlayer.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayer.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayer.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
    }

    @Test
    void whenPlayerOneMakesAPawnPromotionCall_andPawnPromotionIsPending_ShouldPromotePawn() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(1, 0, 2, 2),
            new ChessMoveRequest(0, 0, 2, 3),
            new ChessMoveRequest(6, 0, 1, 0)
        );
        Board initialBoard = new Board(preInitChessMoveRequestList);
        initialBoard.movePiece(1,0,0,0);

        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);

        ArgumentCaptor<BoardEntity> boardCaptor = ArgumentCaptor.forClass(BoardEntity.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));
        when(boardRepository.saveAndFlush(any(BoardEntity.class))).thenReturn(boardEntity);

        JsonObjectBoardResponse boardGameJsonResponse = underTest.processPawnPromotion(CHESS_BOARD_ID, TEST_USER_ONE, "Queen");

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("ONGOING");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("black");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(boardGameJsonResponse.getWinner()).isBlank();

        verify(boardRepository).saveAndFlush(boardCaptor.capture());
        PlayersEntity savedCurrentPlayer = boardCaptor.getValue().getPlayersEntity();
        assertThat(savedCurrentPlayer.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayer.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayer.getActivePlayerUsername()).isEqualTo(TEST_USER_TWO);
    }

    @Test
    void whenPlayerOneMakesAMove_andPlayerTwoMakesMove_gameShouldEndInCheckmate() {
        List<ChessMoveRequest> preInitChessMoveRequestList = List.of(
            new ChessMoveRequest(0, 4, 5, 2),
            new ChessMoveRequest(6, 2, 2, 3),
            new ChessMoveRequest(6, 0, 2, 7)
        );
        Board initialBoard = new Board(preInitChessMoveRequestList);

        BoardEntity boardEntity = boardEntityMapper.fromBoardObject(initialBoard);
        boardEntity.setId(CHESS_BOARD_ID);
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId(TEST_USER_ONE_ID);
        playersEntity.setPlayerOneAppUsername(TEST_USER_ONE);
        playersEntity.setPlayerTwoAppUserId(TEST_USER_TWO_ID);
        playersEntity.setPlayerTwoAppUsername(TEST_USER_TWO);
        playersEntity.setActivePlayerUsername(TEST_USER_ONE);
        boardEntity.setPlayersEntity(playersEntity);

        ChessMoveRequest chessMoveRequestWhite = new ChessMoveRequest(6, 7, 5, 7);
        ChessMoveRequest chessMoveRequestBlack = new ChessMoveRequest(5, 2, 5, 1);


        ArgumentCaptor<BoardEntity> boardCaptor = ArgumentCaptor.forClass(BoardEntity.class);

        when(boardRepository.findById(CHESS_BOARD_ID)).thenReturn(Optional.of(boardEntity));
        when(boardRepository.saveAndFlush(any(BoardEntity.class))).thenReturn(boardEntity);

        underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_ONE, chessMoveRequestWhite);
        JsonObjectBoardResponse boardGameJsonResponse = underTest.processChessMove(CHESS_BOARD_ID, TEST_USER_TWO, chessMoveRequestBlack);

        assertThat(boardGameJsonResponse.getBoardId()).isEqualTo(CHESS_BOARD_ID);
        assertThat(boardGameJsonResponse.getSquares()).isDeepEqualTo(initialBoard.getSquares());
        assertThat(boardGameJsonResponse.getGameState()).isEqualTo("CHECKMATE");
        assertThat(boardGameJsonResponse.getCurrentPlayerColour()).isEqualTo("white");
        assertThat(boardGameJsonResponse.getPawnPromotionPending()).isFalse();
        assertThat(boardGameJsonResponse.getWinner()).isEqualTo(TEST_USER_TWO);

        verify(boardRepository, times(2)).saveAndFlush(boardCaptor.capture());
        PlayersEntity savedCurrentPlayer = boardCaptor.getValue().getPlayersEntity();
        assertThat(savedCurrentPlayer.getPlayerOneAppUserId()).isEqualTo(TEST_USER_ONE_ID);
        assertThat(savedCurrentPlayer.getPlayerOneAppUsername()).isEqualTo(TEST_USER_ONE);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUserId()).isEqualTo(TEST_USER_TWO_ID);
        assertThat(savedCurrentPlayer.getPlayerTwoAppUsername()).isEqualTo(TEST_USER_TWO);
        assertThat(savedCurrentPlayer.getActivePlayerUsername()).isEqualTo(TEST_USER_ONE);
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
