package com.lazychess.chessgame.controller;

import static com.lazychess.chessgame.controller.ControllerConstants.*;
import static com.lazychess.chessgame.controller.ControllerConstants.ADD_PLAYER_TWO_TO_BOARD_PATH;
import static com.lazychess.chessgame.controller.ControllerConstants.MAKE_A_MOVE_PATH;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lazychess.chessgame.config.ValidUuid;
import com.lazychess.chessgame.dto.ChessMoveDto;
import com.lazychess.chessgame.dto.PawnPromotionDto;
import com.lazychess.chessgame.factory.ResponseEntityFactory;
import com.lazychess.chessgame.json.JsonObjectBoardResponse;
import com.lazychess.chessgame.security.AppUserPrincipal;
import com.lazychess.chessgame.security.CustomUserDetailsService;
import com.lazychess.chessgame.service.BoardService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = BASE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BoardController {

    private final BoardService boardService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ResponseEntityFactory responseEntityFactory;

    public BoardController(BoardService boardService,
                           CustomUserDetailsService customUserDetailsService,
                           ResponseEntityFactory responseEntityFactory) {
        this.boardService = boardService;
        this.customUserDetailsService = customUserDetailsService;
        this.responseEntityFactory = responseEntityFactory;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = BOARD_PATH)
    public ResponseEntity<JsonObjectBoardResponse> createBoard() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = customUserDetailsService.loadUserByUsername(principal.getName());

        JsonObjectBoardResponse initialBoardGame = boardService.createInitialBoardGame(appUserPrincipal.getAppUser());
        return responseEntityFactory.toResponseEntity(initialBoardGame);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = GET_BOARD_PATH)
    public ResponseEntity<JsonObjectBoardResponse> getBoard(@PathVariable String boardId) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = customUserDetailsService.loadUserByUsername(principal.getName());

        JsonObjectBoardResponse boardGame = boardService.getBoard(boardId, appUserPrincipal.getUsername());
        return responseEntityFactory.toResponseEntity(boardGame);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = ADD_PLAYER_TWO_TO_BOARD_PATH)
    public ResponseEntity<JsonObjectBoardResponse> playerTwoJoinsBoard(@PathVariable @NotBlank @ValidUuid String boardGameId) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = customUserDetailsService.loadUserByUsername(principal.getName());

        JsonObjectBoardResponse jsonObjectBoardResponse = boardService.playerTwoJoinsGame(boardGameId, appUserPrincipal.getAppUser());
        return responseEntityFactory.toResponseEntity(jsonObjectBoardResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = MAKE_A_MOVE_PATH)
    public ResponseEntity<JsonObjectBoardResponse> makeAMove(@PathVariable String boardGameId, @Valid @RequestBody ChessMoveDto chessMoveDto) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = customUserDetailsService.loadUserByUsername(principal.getName());

        JsonObjectBoardResponse jsonObjectBoardResponse = boardService.processChessMove(boardGameId, appUserPrincipal.getAppUser().getUsername(), chessMoveDto);
        return responseEntityFactory.toResponseEntity(jsonObjectBoardResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = PROMOTE_PAWN_PATH)
    public ResponseEntity<JsonObjectBoardResponse> promotePawn(@PathVariable String boardGameId, @Valid @RequestBody PawnPromotionDto pawnPromotionDto) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = customUserDetailsService.loadUserByUsername(principal.getName());

        JsonObjectBoardResponse jsonObjectBoardResponse = boardService.processPawnPromotion(boardGameId, appUserPrincipal.getAppUser().getUsername(), pawnPromotionDto.upgradedPieceName());
        return responseEntityFactory.toResponseEntity(jsonObjectBoardResponse);
    }
}
