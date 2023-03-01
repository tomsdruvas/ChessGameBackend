package com.lazychess.chessgame.config;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazychess.chessgame.chessgame.Square;
import com.lazychess.chessgame.exception.CannotWriteObjectIntoStringException;
import com.lazychess.chessgame.exception.CannotWriteStringIntoObjectException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SquareListConverter implements AttributeConverter<Square[][], String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Square[][] attribute) {
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        }catch(JsonProcessingException e) {
            throw new CannotWriteObjectIntoStringException("Cannot write list into string");
        }
    }

    @Override
    public Square[][] convertToEntityAttribute(String dbData) {
        try {
            return OBJECT_MAPPER.readValue(dbData, Square[][].class);
        } catch (IOException e) {
            throw new CannotWriteStringIntoObjectException("Cannot write string into object");
        }
    }
}
