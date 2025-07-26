package pet.pet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pet.pet.config.JwtProperties;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey key;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateToken(@NonNull String username, @NonNull Long userId, @NonNull List<String> roles) {
        Claims claims = Jwts.claims().subject(username).build();
        claims.put("userId", userId);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS512) // Используем новый API для подписи
                .compact();
    }

    public boolean validateToken(@NonNull String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // Можно логировать ошибку e.getMessage()
            return false;
        }
    }

    public Claims getClaims(@NonNull String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public String getUsernameFromToken(@NonNull String token) {
        return getClaims(token).getSubject();
    }

    public List<GrantedAuthority> getAuthorities(@NonNull String token) {
        Object rawRoles = getClaims(token).get("roles");
        if (rawRoles instanceof List<?>) {
            return ((List<?>) rawRoles).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> new SimpleGrantedAuthority((String) role))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}