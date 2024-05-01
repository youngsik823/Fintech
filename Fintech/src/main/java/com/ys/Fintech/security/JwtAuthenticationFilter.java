package com.ys.Fintech.security;

import com.ys.Fintech.security.TokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String token = resolveTokenFromRequest(request);

    if (token != null) {
      log.info("Jwt Token Filter is running.... token : {}", token);

      try {
      log.info("Jwt Token : {}", token);
        TokenAccountUserInfo tokenAccountUserInfo = tokenProvider.getTokenAccountUserInfo(token);
        log.debug("TokenAccountUserInfo extracted from token: {}", tokenAccountUserInfo);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(tokenAccountUserInfo.getRole().toString()));
        AbstractAuthenticationToken authority = new UsernamePasswordAuthenticationToken(
            tokenAccountUserInfo,
            null,
            authorityList
        );
        //인증완료 처리시 클라이언트의 요청정보 세팅
        authority.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //스프링 시큐리티 컨테이너에 인증정보 객체 등록
        SecurityContextHolder.getContext().setAuthentication(authority);
      } catch (JwtException e) {
        log.info("토큰이 위조되었습니다.");
      }
    }

    filterChain.doFilter(request, response);
  }

  private String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(TOKEN_HEADER);
    log.info("tokenInfo --- {}", token);
    if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}
