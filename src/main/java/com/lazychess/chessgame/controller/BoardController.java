package com.lazychess.chessgame.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lazychess.chessgame.repository.entity.BoardDao;
import com.lazychess.chessgame.security.AppUserPrincipal;
import com.lazychess.chessgame.security.CustomUserDetailsService;
import com.lazychess.chessgame.service.BoardService;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    private final BoardService boardService;
    private final CustomUserDetailsService customUserDetailsService;

    public BoardController(BoardService boardService, CustomUserDetailsService customUserDetailsService) {
        this.boardService = boardService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "board")
    public BoardDao createBoard() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = customUserDetailsService.loadUserByUsername(principal.getName());

        return boardService.createInitialBoardGame(appUserPrincipal.getAppUser().getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "add-player-two-board/{boardGameId}")
    public BoardDao playerTwoJoinsBoard(@PathVariable String boardGameId) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = customUserDetailsService.loadUserByUsername(principal.getName());

        return boardService.playerTwoJoinsGame(boardGameId, appUserPrincipal.getAppUser().getId());
    }



}
