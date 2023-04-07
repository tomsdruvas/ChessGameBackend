package com.lazychess.chessgame.repository;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.lazychess.chessgame.applicationuser.ApplicationUser;
import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.repository.entity.PlayersDao;
import com.lazychess.chessgame.repository.mapper.BoardDaoMapper;
import com.lazychess.chessgame.service.BoardService;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(classes = SecurityAutoConfiguration.class)
@DataJpaTest(properties = "spring.main.web-application-type=servlet")
class BoardDaoRepositoryIntegrationTest {

    private static final String[] DYNAMIC_FIELDS = {
        "id"
    };

    @Autowired
    private BoardRepository boardRepository;
    private BoardService boardService;

    @BeforeEach
    public void setup() {
        boardService = new BoardService(boardRepository, new BoardDaoMapper());
    }

    @Test
    void insertBoardRecord() {
        BoardDao boardDao = new BoardDao();
        boardDao = boardRepository.saveAndFlush(boardDao);

        assertThat(boardDao).isNotNull();
        assertThat(boardDao.getId()).isNotBlank();
    }

    @Test
    void insertBoardUsingBoardService() {
        JsonObjectBoardResponse boardDao = boardService.createInitialBoardGame(new ApplicationUser("Player1Id", "Player1Username", "Test"));

        assertThat(boardDao.getBoardId()).isNotBlank();

        Optional<BoardDao> retrievedOptional = boardRepository.findById(boardDao.getBoardId());

        assertThat(retrievedOptional.get()).isNotNull();
        BoardDao retrievedBoardDao = retrievedOptional.get();

        assertThat(retrievedBoardDao)
            .usingRecursiveComparison()
            .withComparatorForFields(notNullComparator(), DYNAMIC_FIELDS)
            .isEqualTo(createBoardDaoEntity());
    }

    private Board createBoard() {
        return new Board();
    }

    private BoardDao createBoardDaoEntity() {
        PlayersDao playersDao = new PlayersDao();
        playersDao.setPlayerOneAppUserId("Player1Id");
        playersDao.setPlayerOneAppUsername("Player1Username");
        playersDao.setActivePlayerUsername(playersDao.getPlayerOneAppUsername());
        Board board = createBoard();
        BoardDao boardDao = new BoardDao();

        boardDao.setSquares(board.getSquares());
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());
        boardDao.setPlayersDao(playersDao);

        return boardDao;
    }
}
