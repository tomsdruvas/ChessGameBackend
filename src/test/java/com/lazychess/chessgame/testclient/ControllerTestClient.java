package com.lazychess.chessgame.testclient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Component
public class ControllerTestClient {

    @Autowired(required = false)
    private MockMvc mockMvc;

    public MvcResult performHttpRequest(ControllerTestData testData) throws Exception {
        return testData.requestContent() == null
            ? performHttpRequestWithNoContent(testData)
            : performHttpRequestWithContent(testData);
    }

    private MvcResult performHttpRequestWithNoContent(ControllerTestData testData) throws Exception {
        return mockMvc
            .perform(request(testData.httpMethod(), testData.endpointUrl())
                .headers(testData.requestHeaders()))
            .andReturn();
    }

    private MvcResult performHttpRequestWithContent(ControllerTestData testData) throws Exception {
        return mockMvc
            .perform(request(testData.httpMethod(), testData.endpointUrl())
                .headers(testData.requestHeaders())
                .content(testData.requestContent()))
            .andReturn();
    }
}
