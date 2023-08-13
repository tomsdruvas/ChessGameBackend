package com.lazychess.chessgame.repository.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity(name = "refresh_token")
public class RefreshToken {

  @Id
  @Column(name = "refresh_token", nullable = false, unique = true)
  private String token;

  @OneToOne
  @JoinColumn(name = "cgb_app_user_refresh_token_id", referencedColumnName = "cgb_app_user_id")
  private ApplicationUser applicationUser;

  @Column(nullable = false)
  private Instant expiryDate;

  public RefreshToken() {
  }

  public ApplicationUser getApplicationUser() {
    return applicationUser;
  }

  public void setApplicationUser(ApplicationUser applicationUser) {
    this.applicationUser = applicationUser;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }

}
