package com.lazychess.chessgame.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static testUtil.GenericComparators.DYNAMIC_FIELDS;
import static testUtil.GenericComparators.notNullComparator;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lazychess.chessgame.repository.entity.ApplicationUser;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.repository.entity.BoardEntity;
import com.lazychess.chessgame.repository.entity.LatestMoveEntity;
import com.lazychess.chessgame.repository.entity.PlayersEntity;
import com.lazychess.chessgame.repository.mapper.BoardEntityMapper;
import com.lazychess.chessgame.service.BoardService;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(classes = SecurityAutoConfiguration.class)
@DataJpaTest(properties = "spring.main.web-application-type=servlet")
class BoardEntityRepositoryIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;
    private BoardService boardService;

    @BeforeEach
    public void setup() {
        boardService = new BoardService(boardRepository, new BoardEntityMapper());
    }

    @Test
    void insertBoardRecord() {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity = boardRepository.saveAndFlush(boardEntity);

        assertThat(boardEntity).isNotNull();
        assertThat(boardEntity.getId()).isNotBlank();
    }

    @Test
    void insertBoardUsingBoardService() {
        JsonObjectBoardResponse boardEntity = boardService.createInitialBoardGame(new ApplicationUser("Player1Id", "Player1Username", "Test"));

        assertThat(boardEntity.getBoardId()).isNotBlank();

        Optional<BoardEntity> retrievedOptional = boardRepository.findById(boardEntity.getBoardId());

        assertThat(retrievedOptional.get()).isNotNull();
        BoardEntity retrievedBoardEntity = retrievedOptional.get();

        assertThat(retrievedBoardEntity)
            .usingRecursiveComparison()
            .withComparatorForFields(notNullComparator(), DYNAMIC_FIELDS)
            .isEqualTo(createBoardEntity());
    }

    private Board createBoard() {
        return new Board();
    }

    private BoardEntity createBoardEntity() {
        PlayersEntity playersEntity = new PlayersEntity();
        playersEntity.setPlayerOneAppUserId("Player1Id");
        playersEntity.setPlayerOneAppUsername("Player1Username");
        playersEntity.setActivePlayerUsername(playersEntity.getPlayerOneAppUsername());
        Board board = createBoard();
        BoardEntity boardEntity = new BoardEntity();

        LatestMoveEntity latestMove = new LatestMoveEntity();
        latestMove.setColumn(0);
        latestMove.setRow(0);

        boardEntity.setSquares(board.getSquares());
        boardEntity.setStateOfTheGame(board.getStateOfTheGame());
        boardEntity.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardEntity.setPlayersEntity(playersEntity);
        boardEntity.setLatestMove(latestMove);

        return boardEntity;
    }
}
