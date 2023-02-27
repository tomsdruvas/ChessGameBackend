package com.lazychess.chessgame.testclient;

import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;

import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public interface ControllerTestData {

    HttpMethod httpMethod();

    String endpointUrl();

    HttpHeaders requestHeaders();

    String requestContent() throws Exception;

    int expectedResponseStatusCode();

    HttpHeaders expectedResponseHeaders();

    default Set<String> ignoredResponseHeaders() {
        return Set.of(CONTENT_LENGTH);
    }

    String expectedResponseContent() throws Exception;
}