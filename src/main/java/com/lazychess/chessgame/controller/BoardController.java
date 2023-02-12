package com.lazychess.chessgame.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.service.BoardService;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "board")
    public BoardDao getBoard() {
        return boardService.createInitialBoardGame();
    }

}
