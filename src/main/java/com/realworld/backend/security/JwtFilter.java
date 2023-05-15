package com.realworld.backend.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTHORIZATION);
        try {
            tokenProvider.resolveHeader(header)
                    .map(tokenProvider::getAuthentication)
                    .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();
            return;
        }
        filterChain.doFilter(request, response);
    }

}
