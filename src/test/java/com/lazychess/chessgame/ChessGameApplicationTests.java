package com.lazychess.chessgame;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lazychess.chessgame.controller.BoardController;
import com.lazychess.chessgame.service.BoardService;

@SpringBootTest
class ChessGameApplicationTests {

	@Autowired
	private BoardController boardController;

	@Autowired
	private BoardService boardService;

	@Test
	void contextLoads() {
		assertThat(boardController).isNotNull();
		assertThat(boardService).isNotNull();
	}
}
