package com.lazychess.chessgame.controllerintegrationtest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.lazychess.chessgame.applicationuser.ApplicationUserRepository;

import wiremock.com.fasterxml.jackson.core.JsonProcessingException;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.databind.ObjectWriter;
import wiremock.com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class ApplicationUserControllerIntegrationTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private final String testUser1 = "test_user1";

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_roles", "chess_game_board", "chess_game_user");
    }

    @Test
    void userShouldBeAbleToRegisterAnAccount() throws Exception {
        mockMvc.perform(post("/registration")
            .contentType(APPLICATION_JSON_UTF8)
            .content(getMoveJsonRequestBody(testUser1, "admin", "admin")))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.username").value("test_user1"))
            .andReturn();
        assertUsernameExistsInDatabase();
    }

    @Test
    void userShouldNotBeAbleToRegisterAnAccountWhenUsernameAlreadyTaken() throws Exception {
        mockMvc.perform(post("/registration")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getMoveJsonRequestBody(testUser1, "admin", "admin")))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.username").value("test_user1"))
            .andReturn();

        mockMvc.perform(post("/registration")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getMoveJsonRequestBody(testUser1, "admin", "admin")))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.Message").value("Username already exists"))
            .andReturn();
        assertUsernameExistsInDatabase();
    }

    @Test
    void userShouldNotBeAbleToRegisterAnAccountWhenPasswordsDontMatch() throws Exception {
        mockMvc.perform(post("/registration")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getMoveJsonRequestBody(testUser1, "admin", "admin1")))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.Message").value("Passwords don't match"))
            .andReturn();
        boolean existsByUsername = applicationUserRepository.existsByUsername(testUser1);
        assertThat(existsByUsername).isFalse();
    }

    @Test
    void userShouldNotBeAbleToRegisterAnAccountWhenPasswordsAreTooShort() throws Exception {
        mockMvc.perform(post("/registration")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getMoveJsonRequestBody(testUser1, "abc", "abc")))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.Message").value("Password needs to be minimum 5 characters"))
            .andReturn();
        boolean existsByUsername = applicationUserRepository.existsByUsername(testUser1);
        assertThat(existsByUsername).isFalse();
    }

    @Test
    void userShouldNotBeAbleToRegisterAnAccountWhenUsernameIsBlank() throws Exception {
        mockMvc.perform(post("/registration")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getMoveJsonRequestBody("", "admin", "admin")))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.Message").value("Username needs to be minimum 3 characters"))
            .andReturn();
        boolean existsByUsername = applicationUserRepository.existsByUsername(testUser1);
        assertThat(existsByUsername).isFalse();
    }

    @Test
    void userShouldNotBeAbleToRegisterAnAccountWhenUsernameIsTooShort() throws Exception {
        mockMvc.perform(post("/registration")
                .contentType(APPLICATION_JSON_UTF8)
                .content(getMoveJsonRequestBody("ab", "admin", "admin")))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.Message").value("Username needs to be minimum 3 characters"))
            .andReturn();
        boolean existsByUsername = applicationUserRepository.existsByUsername(testUser1);
        assertThat(existsByUsername).isFalse();
    }

    void assertUsernameExistsInDatabase() {
        boolean existsByUsername = applicationUserRepository.existsByUsername(testUser1);
        assertThat(existsByUsername).isTrue();
    }

    private String getMoveJsonRequestBody(String username, String password, String confirmPassword) throws JsonProcessingException {
        Map<String,Object> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("confirmPassword", confirmPassword);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(body);
    }
}
