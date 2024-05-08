package com.ys.Fintech.security;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

  @Value("${spring.jwt.secret}")
  private String SECRET_KEY;

  public String createToken(AccountUser accountUser) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", accountUser.getEmail());
    claims.put("phoneNum", accountUser.getPhoneNum());
    claims.put("name", accountUser.getName());
    claims.put("role", accountUser.getRole());

    Date expiry = new Date(System.currentTimeMillis() + 3600000); // 1시간 후 만료

    return Jwts.builder()
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512)
        .addClaims(claims)
        .setIssuer("youngsik")
        .setIssuedAt(new Date())
        .setExpiration(expiry)
        .setSubject(String.valueOf(accountUser.getId()))
        .compact();
  }


  public TokenAccountUserInfo getTokenAccountUserInfo(String token) {
      Claims claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).parseClaimsJws(token).getBody();

    log.info("claims : {}",claims);

      return TokenAccountUserInfo.builder()
          .id(Long.valueOf(claims.getSubject()))
          .email(claims.get("email", String.class))
          .phoneNum(claims.get("phoneNum", String.class))
          .name(claims.get("name", String.class))
          .role(Role.valueOf(claims.get("role", String.class)))
          .build();

  }
}
