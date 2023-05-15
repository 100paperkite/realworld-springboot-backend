package com.realworld.backend.security;

import com.realworld.backend.exception.RealWorldException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.realworld.backend.exception.RealWorldError.INVALID_AUTH_TOKEN;

@Component
public class TokenProvider {
    private static final String TOKEN_TYPE_NAME = "Token";

    private final String secret;
    private final long expiration;
    @Autowired
    private CustomUserDetails userDetails;
    private Key key;

    public TokenProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.token.expiration}") long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }


    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            throw new RealWorldException(INVALID_AUTH_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Optional<String> resolveToken(String header) {
        return Optional.ofNullable(header)
                .filter(token -> token.startsWith(TOKEN_TYPE_NAME + " "))
                .map(token -> token.substring(7));
    }
}
