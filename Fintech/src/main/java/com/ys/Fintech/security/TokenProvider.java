package com.ys.Fintech.security;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.service.AccountUserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor

public class TokenProvider {
  private static final String KEY_GRADE = "role";

  @Value("${spring.jwt.secret}")
  private String SECRET_KEY;

  private AccountUserService accountUserService;

  public void setAccountUserService(AccountUserService accountUserService) {
    this.accountUserService = accountUserService;
  }

  public String createToken(AccountUser accountUser) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", accountUser.getEmail());
    claims.put("role", accountUser.getRole());

    Date expiry = Date.from(
        Instant.now().plus(1, ChronoUnit.HOURS) // 현재 시간으로부터 1시간
    );

    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
        .setIssuer("youngsik")  // 발급자 정보
        .setIssuedAt(new Date())  // 토큰 생성시간
        .setExpiration(expiry)  // 토큰 만료시간
        .setSubject(String.valueOf(accountUser.getId())) // 토큰을 식별할 수 있는 주요데이터
        .setClaims(claims) //데이터 추가
        .compact();

  }

  public Authentication getAuthentication(String jwt) {
    UserDetails userDetails = accountUserService.loadUserByUsername(getUsername(jwt));

    // 사용자 정보와 사용자 권한 정보를 포함
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return perseClaims(token).getSubject();
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }
    Claims claims = perseClaims(token);
    return claims.getExpiration().before(new Date()); // 토큰 만료시간 검증
  }

  // token 유효성 검사
  private Claims perseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
