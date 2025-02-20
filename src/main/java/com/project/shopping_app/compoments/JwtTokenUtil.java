package com.project.shopping_app.compoments;

import com.project.shopping_app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {
  @Value("${jwt.expiration}")
  private long expiration; // luu trong bien moi truonwg

  @Value("${jwt.secretKey}")
  private String secretKey;

  public String generateToken(User user) throws Exception {
    // properties => claims
    Map<String, Object> claims = new HashMap<>();
    claims.put("phoneNumber", user.getPhoneNumber());
    try {
      String token = Jwts.builder()
            .setClaims(claims) // how to extract claim ...
            .setSubject(user.getPhoneNumber())
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
      return token;
    } catch (Exception e) {
      log.error("Cannot create jwt token" + e.getMessage());
      throw new DataIntegrityViolationException("Cannot create jwt token");
    }
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);

  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
          .setSigningKey(getSignInKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
  }

  private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
//    final Claims claims = this.extractAllClaims(token);
//    return claimsResolver.apply(claims);
  }

  // checj expiration
  public boolean isTokenExpired(String token) {
    Date expirationDate = extractClaims(token, Claims::getExpiration); // dung method referent
    return expirationDate.before(new Date());
  }

  public String extractPhoneNumber(String token) {
    return extractClaims(token, Claims::getSubject);
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    String phoneNumber = extractPhoneNumber(token);
    return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }
}
