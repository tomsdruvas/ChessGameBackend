package com.lazychess.chessgame.controller;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Objects;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lazychess.chessgame.json.JsonObjectErrorResponse;

@ControllerAdvice(assignableTypes = BoardController.class)
@Order(HIGHEST_PRECEDENCE)
public class BoardControllerAdvice {

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

    public static JsonObjectErrorResponse buildJsonObjectErrorResponse(String message) {
        return JsonObjectErrorResponse.newBuilder()
            .message(message)
            .build();
    }
}
