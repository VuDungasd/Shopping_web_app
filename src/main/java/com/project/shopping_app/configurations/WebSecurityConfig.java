package com.project.shopping_app.configurations;

import com.project.shopping_app.compoments.JwtTokenUtil;
import com.project.shopping_app.filters.JwtTokenFilter;
import com.project.shopping_app.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtTokenFilter jwtTokenFilter;

  @Value("${api.prefix}")
  private String prefix;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
          .csrf(AbstractHttpConfigurer::disable)
          .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
          .authorizeHttpRequests(requests -> {
            requests
                  .requestMatchers(
                        String.format("%s/users/register", prefix),
                        String.format("%s/users/login", prefix)
                  )
                  .permitAll()
                  .requestMatchers(GET,
                        String.format("%s/categories**", prefix)).hasAnyRole(Role.USER, Role.ADMIN)

                  .requestMatchers(POST,
                        String.format("%s/categories/**", prefix)).hasAnyRole(Role.ADMIN)

                  .requestMatchers(PUT,
                        String.format("%s/categories/**", prefix)).hasAnyRole(Role.ADMIN)

                  .requestMatchers(DELETE,
                        String.format("%s/categories/**", prefix)).hasAnyRole(Role.ADMIN)

                  .requestMatchers(GET,
                        String.format("%s/products**", prefix)).hasAnyRole(Role.USER, Role.ADMIN)

                  .requestMatchers(POST,
                        String.format("%s/products/**", prefix)).hasAnyRole(Role.ADMIN)

                  .requestMatchers(PUT,
                        String.format("%s/products/**", prefix)).hasAnyRole(Role.ADMIN)

                  .requestMatchers(DELETE,
                        String.format("%s/products/**", prefix)).hasAnyRole(Role.ADMIN)

                  .requestMatchers(POST,
                        String.format("%s/orders/**", prefix)).hasRole(Role.USER)

                  .requestMatchers(GET,
                        String.format("%s/orders/**", prefix)).hasAnyRole(Role.USER, Role.ADMIN)

                  .requestMatchers(PUT,
                        String.format("%s/orders/**", prefix)).hasRole(Role.ADMIN)
                  .requestMatchers(PUT,
                        String.format("%s/orders/users/**", prefix)).hasRole(Role.USER)

                  .requestMatchers(DELETE,
                        String.format("%s/orders/**", prefix)).hasRole(Role.ADMIN)

                  .requestMatchers(POST,
                        String.format("%s/order_details/**", prefix)).hasAnyRole(Role.USER)

                  .requestMatchers(GET,
                        String.format("%s/order_details/**", prefix)).hasAnyRole(Role.USER, Role.ADMIN)

                  .requestMatchers(PUT,
                        String.format("%s/order_details/**", prefix)).hasRole(Role.ADMIN)

                  .requestMatchers(DELETE,
                        String.format("%s/order_details/**", prefix)).hasRole(Role.ADMIN)

                  .anyRequest().authenticated();
          });
    return http.build();
  }
}
