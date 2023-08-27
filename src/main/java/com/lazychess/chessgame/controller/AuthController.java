package com.lazychess.chessgame.controller;

import static com.lazychess.chessgame.controller.ControllerConstants.BASE_PATH;
import static com.lazychess.chessgame.controller.ControllerConstants.LOGIN_PATH;
import static com.lazychess.chessgame.controller.ControllerConstants.LOGOUT_PATH;
import static com.lazychess.chessgame.controller.ControllerConstants.REFRESH_TOKEN_PATH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lazychess.chessgame.dto.AccessTokenDto;
import com.lazychess.chessgame.security.AppUserPrincipal;
import com.lazychess.chessgame.security.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = BASE_PATH)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @CrossOrigin
    @PostMapping(LOGIN_PATH)
    public ResponseEntity<AccessTokenDto> login(Authentication authentication) {
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        String userId = principal.getAppUser().getId();
        String username = principal.getAppUser().getUsername();

        ResponseCookie refreshTokenCookie = tokenService.createAndPersistRefreshTokenCookie(userId);

        logger.debug("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateAccessToken(username);
        logger.debug("Token granted: {}", token);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(new AccessTokenDto(token));
    }

    @PostMapping(REFRESH_TOKEN_PATH)
    public ResponseEntity<AccessTokenDto> refreshToken(HttpServletRequest request) {
        return tokenService.refreshAccessToken(request);
    }

    @PostMapping(LOGOUT_PATH)
    public ResponseEntity<String> logoutUser(Authentication authentication) {
        String username = authentication.getName();
        tokenService.deleteByApplicationUserUsername(username);

        ResponseCookie jwtRefreshCookie = tokenService.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
            .build();
    }
}