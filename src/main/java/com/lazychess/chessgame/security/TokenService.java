package com.lazychess.chessgame.security;

import static com.lazychess.chessgame.controller.ControllerConstants.REFRESH_TOKEN_PATH;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import com.lazychess.chessgame.dto.AuthDetailsDto;
import com.lazychess.chessgame.exception.RefreshTokenException;
import com.lazychess.chessgame.repository.ApplicationUserRepository;
import com.lazychess.chessgame.repository.RefreshTokenRepository;
import com.lazychess.chessgame.repository.entity.ApplicationUser;
import com.lazychess.chessgame.repository.entity.RefreshToken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {

    private static final String REFRESH_COOKIE_NAME = "refresh_cookie";

    private final JwtEncoder encoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public TokenService(@Lazy JwtEncoder encoder,
                        RefreshTokenRepository refreshTokenRepository,
                        ApplicationUserRepository applicationUserRepository) {
        this.encoder = encoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    public String generateAccessToken(String username) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.HOURS))
                .subject(username)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public ResponseEntity<AuthDetailsDto> refreshAccessToken(HttpServletRequest request) {
        String refreshTokenCookie = getJwtRefreshFromCookies(request);

        if(refreshTokenCookie != null && !refreshTokenCookie.isEmpty()) {
            Optional<RefreshToken> tokenOptional = findByToken(refreshTokenCookie);
            if(tokenOptional.isPresent()) {
                RefreshToken refreshToken = tokenOptional.get();
                verifyExpiration(refreshToken);
                ApplicationUser applicationUser = refreshToken.getApplicationUser();
                String accessToken = generateAccessToken(applicationUser.getUsername());
                return ResponseEntity.ok()
                    .body(new AuthDetailsDto(accessToken, applicationUser.getUsername(), List.of("User")));
            } else {
                throw new RefreshTokenException(refreshTokenCookie +
                    "Refresh token is not in database!");
            }
        }

        throw new RefreshTokenException("Refresh Token is empty!");
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    private RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setApplicationUser(applicationUserRepository.findById(userId));
        refreshToken.setExpiryDate(Instant.now().plusSeconds(9999999L));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken() + " - Refresh token was expired. Please make a new login request");
        }

        return token;
    }

    @Transactional
    public void deleteByApplicationUserUsername(String username) {
        refreshTokenRepository.deleteByApplicationUserUsername(username);
    }

    public ResponseCookie createAndPersistRefreshTokenCookie(String userId) {
        RefreshToken refreshToken = createRefreshToken(userId);
        return generateRefreshJwtCookie(refreshToken.getToken());
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, REFRESH_COOKIE_NAME);
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "").path(REFRESH_TOKEN_PATH).build();
    }

    private ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(REFRESH_COOKIE_NAME, refreshToken, REFRESH_TOKEN_PATH);
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value).path(path).maxAge(24L * 60L * 60L).httpOnly(true).build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
