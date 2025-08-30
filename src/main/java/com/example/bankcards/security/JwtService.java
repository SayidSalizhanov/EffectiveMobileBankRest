package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис генерации и верификации JWT токенов.
 */
@Service
@Profile("!test")
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Извлекает логин пользователя из токена.
     * @param token jwt-токен, из которого извлекается логин
     * @return логин
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает произвольное поле из токена.
     * @param token jwt-токен, из которого извлекаются claims
     * @param claimsResolver функция, которая принимает Claims и возвращает конкретное значение claim типа T
     * @return извлеченное значение claim типа T
     * @param <T> тип извлекаемого значения claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Генерирует токен на основе данных пользователя.
     * @param userDetails данные пользователя
     * @return jwt-токен
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Генерирует токен с дополнительными полями.
     * @param extraClaims дополнительные атрибуты (claims)
     * @param userDetails данные пользователя
     * @return jwt-токен
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Проверяет валидность токена.
     * @param token jwt-токен
     * @param userDetails данные пользователя
     * @return логический тип (true/false)
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Проверяет, истёк ли срок действия токена.
     * @param token jwt-токен
     * @return логический тип (true/false)
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Получает дату истечения токена.
     * @param token jwt-токен
     * @return дата истечения
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает всю информацию из токена.
     * @param token jwt-токен
     * @return информация токена (claims)
     */
    private Claims extractAllClaims(String token) {
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token);
        return jws.getPayload();
    }

    /**
     * Создаёт ключ для проверки подписи токена.
     * @return ключ для проверки подписи
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}