package pet.pet.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pet.pet.config.JwtProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private Key key;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Long userId, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        Object userId = claims.get("userId");
        return userId instanceof Integer ? ((Integer) userId).longValue() : (Long) userId;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !getExpirationDateFromToken(token).before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            System.out.println("Invalid JWT: " + ex.getMessage());
            return false;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaims(token).getExpiration();
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = getClaims(token);
        Object rawRoles = claims.get("roles");

        if (rawRoles instanceof List<?>) {
            return ((List<?>) rawRoles).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> new SimpleGrantedAuthority((String) role))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
