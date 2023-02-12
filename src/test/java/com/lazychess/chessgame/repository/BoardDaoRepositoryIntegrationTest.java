package com.lazychess.chessgame.repository;

import static TestUtil.GenericComparators.notNullComparator;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lazychess.chessgame.chessgame.Board;
import com.lazychess.chessgame.dto.PreInitialisationMoveDto;
import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.repository.mapper.BoardDaoMapper;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(classes = SecurityAutoConfiguration.class)
@DataJpaTest(properties = "spring.main.web-application-type=servlet")
public class BoardDaoRepositoryIntegrationTest {

    private static final String[] DYNAMIC_FIELDS = {
        "id"
    };

    @Autowired
    private BoardRepository boardRepository;
    private BoardFacade boardFacade;

    @Before
    public void setup() {
        boardFacade = new BoardFacade(boardRepository, new BoardDaoMapper());
    }

    @Test
    public void insertBoardRecord() {
        BoardDao boardDao = new BoardDao();
        boardDao = boardRepository.saveAndFlush(boardDao);

        assertThat(boardDao).isNotNull();
        assertThat(boardDao.getId()).isNotBlank();
    }

    @Test
    public void insertBoardUsingBoardFacade() {
        Board board = createBoard();

        BoardDao boardDao = boardFacade.persistBoard(board);

        assertThat(boardDao.getId()).isNotBlank();

        Optional<BoardDao> retrievedOptional = boardRepository.findById(boardDao.getId());

        assertThat(retrievedOptional.get()).isNotNull();
        BoardDao retrievedBoardDao = retrievedOptional.get();

        assertThat(retrievedBoardDao)
            .usingRecursiveComparison()
            .withComparatorForFields(notNullComparator(), DYNAMIC_FIELDS)
            .isEqualTo(createBoardDaoEntity());
    }

    @Test
    public void insertChangedBoardUsingBoardFacade() {
        Board board = createChangedBoard();

        BoardDao boardDao = boardFacade.persistBoard(board);

        assertThat(boardDao.getId()).isNotBlank();

        Optional<BoardDao> retrievedOptional = boardRepository.findById(boardDao.getId());

        assertThat(retrievedOptional.get()).isNotNull();
        BoardDao retrievedBoardDao = retrievedOptional.get();

        assertThat(retrievedBoardDao)
            .usingRecursiveComparison()
            .withComparatorForFields(notNullComparator(), DYNAMIC_FIELDS)
            .isEqualTo(createChangedBoardDaoEntity());
    }

    private Board createBoard() {
        return new Board();
    }

    private Board createChangedBoard() {
        List<PreInitialisationMoveDto> preInitialisationMoveDtos = List.of(
            new PreInitialisationMoveDto(1, 0, 3, 0),
            new PreInitialisationMoveDto(6, 6, 4, 6));
        return new Board(preInitialisationMoveDtos);
    }

    private BoardDao createBoardDaoEntity() {
        Board board = createBoard();
        BoardDao boardDao = new BoardDao();

        boardDao.setSquares(board.getSquares());
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());

        return boardDao;
    }

    private BoardDao createChangedBoardDaoEntity() {
        Board board = createChangedBoard();
        BoardDao boardDao = new BoardDao();

        boardDao.setSquares(board.getSquares());
        boardDao.setStateOfTheGame(board.getStateOfTheGame());
        boardDao.setCurrentPlayerColour(board.getCurrentPlayerColourState());

        return boardDao;
    }
}
