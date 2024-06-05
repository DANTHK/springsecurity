package top.shang.springsecurity.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import top.shang.springsecurity.properties.RsaKeyProperties;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RsaKeyProperties rsaKeyProperties;

    @Value("${token.expiration.days:3}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000 * 60 * 60 * 24))
                .signWith(rsaKeyProperties.getPrivateKey(), Jwts.SIG.RS512)
                .compact();
    }

    public String generateRefreshToken(Map<String, Objects> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 2 * 1000 * 60 * 60 * 24))
                .signWith(rsaKeyProperties.getPrivateKey(), Jwts.SIG.RS512)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(rsaKeyProperties.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validate(UserDetails user, String token) {
        Claims claims = getClaims(token);
        return Objects.equals(user.getUsername(), claims.getSubject()) && claims.getExpiration().after(Date.from(Instant.now()));
    }
}
