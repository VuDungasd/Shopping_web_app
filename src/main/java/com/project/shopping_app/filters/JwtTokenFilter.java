package com.project.shopping_app.filters;

import com.project.shopping_app.compoments.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  @Value("${api.prefix}")
  private String prefix;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
        throws ServletException, IOException {
    if(isBypassToken(request)){
      filterChain.doFilter(request, response);
    }
    final String authHeader = request.getHeader("Authorization");
    if(authHeader != null && authHeader.startsWith("Bearer ")) {
      final String token = authHeader.substring(7);
      final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);

      if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
        if(jwtTokenUtil.validateToken(token, userDetails)){
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null , userDetails.getAuthorities());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    }
    filterChain.doFilter(request, response);

//    filterChain.doFilter(request, response);  // enable bypass
  }

  private Boolean isBypassToken(@NonNull HttpServletRequest request) {
    final List<Pair<String, String>> bypassTokens = Arrays.asList(
          Pair.of(String.format("%s/products", prefix), "GET"),
          Pair.of(String.format("%s/categories", prefix), "GET"),
          Pair.of(String.format("%s/users/login", prefix), "POST"),
          Pair.of(String.format("%s/users/register", prefix), "POST")
    );
    for (Pair<String, String> bypassToken : bypassTokens) {
      if (request.getServletPath().contains(bypassToken.getFirst()) && request.getMethod().equals(bypassToken.getSecond())) {
        return true;
      }
    }
    return false;
  }
}
