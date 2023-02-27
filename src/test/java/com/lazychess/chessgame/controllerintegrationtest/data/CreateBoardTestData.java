package com.lazychess.chessgame.controllerintegrationtest.data;

import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.lazychess.chessgame.testclient.ControllerTestData;

public class CreateBoardTestData implements ControllerTestData {

    public static final String ENDPOINT_URL_PATH = "/board";


    @Override
    public HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String endpointUrl() {
        return ENDPOINT_URL_PATH;
    }

    @Override
    public HttpHeaders requestHeaders() {
        return null;
    }

    @Override
    public String requestContent() throws Exception {
        return null;
    }

    @Override
    public int expectedResponseStatusCode() {
        return 201;
    }

    @Override
    public HttpHeaders expectedResponseHeaders() {
        return null;
    }

    @Override
    public Set<String> ignoredResponseHeaders() {
        return ControllerTestData.super.ignoredResponseHeaders();
    }

    @Override
    public String expectedResponseContent() throws Exception {
        return null;
    }
}
