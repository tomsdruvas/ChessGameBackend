package com.lazychess.chessgame.controller;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = BoardController.class)
@Order(HIGHEST_PRECEDENCE)
public class BoardControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> invalidChessMove(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
