package com.lazychess.chessgame.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import com.lazychess.chessgame.repository.ApplicationUserRepository;
import com.lazychess.chessgame.repository.RefreshTokenRepository;
import com.lazychess.chessgame.repository.entity.RefreshToken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class RefreshTokenService {

  private static final String REFRESH_COOKIE_NAME = "refresh_cookie";

  private final RefreshTokenRepository refreshTokenRepository;
  private final ApplicationUserRepository applicationUserRepository;

  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, ApplicationUserRepository applicationUserRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.applicationUserRepository = applicationUserRepository;
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
      throw new RuntimeException(token.getToken() + "Refresh token was expired. Please make a new signin request");
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
    return ResponseCookie.from(REFRESH_COOKIE_NAME, "").path("/refreshtoken").build();
  }

  private ResponseCookie generateRefreshJwtCookie(String refreshToken) {
    return generateCookie(REFRESH_COOKIE_NAME, refreshToken, "/refreshtoken");
  }

  private ResponseCookie generateCookie(String name, String value, String path) {
    return ResponseCookie.from(name, value).path(path).maxAge(24 * 60 * 60).httpOnly(true).build();
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
