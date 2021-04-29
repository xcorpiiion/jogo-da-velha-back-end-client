package br.com.unip.jogodavelha.security.utils;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.System.currentTimeMillis;

@Slf4j
@Getter
@NoArgsConstructor
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username) {
        log.info("Gerando token...");
        final String token = builder().setSubject(username).setExpiration(new DateTime().plus(this.getExpiration()).toDate())
                .signWith(HS512, this.getSecret().getBytes()).compact();
        log.info("Token gerado com sucesso");
        return token;
    }

    public boolean isTokenValido(String token) {
        log.info("Verificando se token é valido...");
        Claims claims = getClaims(token);
        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date(currentTimeMillis());
            if (username != null && expirationDate != null && currentDate.before(expirationDate)) {
                log.info("Token validado com sucesso");
                return true;
            }
        }
        log.info("Token não é valido");
        return false;
    }

    private Claims getClaims(String token) {
        try {
            return parser().setSigningKey(this.getSecret().getBytes()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return null;
        }
    }

    public String getUsername(String token) {
        log.info("Tentando buscar o username");
        Claims claims = getClaims(token);
        if (claims != null) {
            log.info("User name retornado com sucesso");
            return claims.getSubject();
        }
        log.info("Não foi possivel retornar o username");
        return null;
    }
}
