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

}
