package com.lazychess.chessgame.controller;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lazychess.chessgame.json.JsonObjectErrorResponse;

@ControllerAdvice(assignableTypes = BoardController.class)
@Order(HIGHEST_PRECEDENCE)
public class BoardControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(BoardControllerAdvice.class);
    
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<JsonObjectErrorResponse> invalidChessMove(RuntimeException e) {
        return ResponseEntity.badRequest().body(buildJsonObjectErrorResponse(e.getMessage()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonObjectErrorResponse> invalidRequestBody(MethodArgumentNotValidException e) {
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest().body(buildJsonObjectErrorResponse(defaultMessage));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<JsonObjectErrorResponse> handleBadRequest(HttpMessageNotReadableException e) {
        logger.warn("Returning HTTP 400 Bad Request", e);
        return ResponseEntity.badRequest().body(buildJsonObjectErrorResponse(e.getMessage()));
    }

    public static JsonObjectErrorResponse buildJsonObjectErrorResponse(String message) {
        return JsonObjectErrorResponse.newBuilder()
            .message(message)
            .build();
    }
}
