package com.lazychess.chessgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lazychess.chessgame.repository.BoardFacade;
import com.lazychess.chessgame.repository.BoardRepository;
import com.lazychess.chessgame.repository.mapper.BoardDaoMapper;

@Configuration
public class RepositoryConfig {

    @Bean
    BoardFacade boardFacade(BoardRepository boardRepository, BoardDaoMapper boardDaoMapper) {
        return new BoardFacade(boardRepository, boardDaoMapper);
    }
}
