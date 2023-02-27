package com.lazychess.chessgame.factory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.lazychess.chessgame.json.JsonObjectBoardResponse;

@Component
public class ResponseEntityFactory {

    public ResponseEntity<JsonObjectBoardResponse> toResponseEntity(JsonObjectBoardResponse jsonObjectBoardResponse) {
        return new ResponseEntity<>(jsonObjectBoardResponse, HttpStatus.CREATED);
    }
}
