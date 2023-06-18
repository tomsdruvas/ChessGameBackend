package com.lazychess.chessgame.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lazychess.chessgame.dto.AccessTokenDto;
import com.lazychess.chessgame.security.TokenService;

@RestController
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @CrossOrigin
    @PostMapping("/token")
    public ResponseEntity<AccessTokenDto> token(Authentication authentication) {

        LOG.debug("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Token granted: {}", token);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization","Bearer "+
            token);

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(new AccessTokenDto(token));
    }
}