package com.lazychess.chessgame.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lazychess.chessgame.repository.entity.BoardDao;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardDaoRepositoryIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoardRecord() {
        BoardDao boardDao = new BoardDao();
        boardDao = boardRepository.saveAndFlush(boardDao);

        assertThat(boardDao).isNotNull();
    }
}
