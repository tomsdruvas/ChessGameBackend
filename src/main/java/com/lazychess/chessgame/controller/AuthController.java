package com.lazychess.chessgame.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lazychess.chessgame.dto.AccessTokenDto;
import com.lazychess.chessgame.repository.entity.RefreshToken;
import com.lazychess.chessgame.security.AppUserPrincipal;
import com.lazychess.chessgame.security.TokenService;
import com.lazychess.chessgame.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(TokenService tokenService,
                          RefreshTokenService refreshTokenService) {
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @CrossOrigin
    @PostMapping("/token")
    public ResponseEntity<AccessTokenDto> token(Authentication authentication) {
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        String userId = principal.getAppUser().getId();
        String username = principal.getAppUser().getUsername();

        ResponseCookie refreshTokenCookie = refreshTokenService.createAndPersistRefreshTokenCookie(userId);

        LOG.debug("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(username);
        LOG.debug("Token granted: {}", token);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(new AccessTokenDto(token));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<AccessTokenDto> refreshToken(HttpServletRequest request) {
        String refreshToken = refreshTokenService.getJwtRefreshFromCookies(request);

        if(refreshToken != null && !refreshToken.isEmpty()) {
            return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getApplicationUser)
                .map(user -> {
                    String accessToken = tokenService.generateToken(user.getUsername());
                    return ResponseEntity.ok()
                        .body(new AccessTokenDto(accessToken));
                })
                .orElseThrow(() -> new RuntimeException(refreshToken +
                    "Refresh token is not in database!"));
        }

        throw new RuntimeException("Refresh Token is empty!");
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        String username = authentication.getName();
        refreshTokenService.deleteByApplicationUserUsername(username);

        ResponseCookie jwtRefreshCookie = refreshTokenService.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
            .build();
    }
}