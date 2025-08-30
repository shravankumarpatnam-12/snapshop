package com.ecommerce.snapshop.security.jwt;

import com.ecommerce.snapshop.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtExpirationInMilliseconds}")
    private int jwtExpirationInMilliseconds;
    @Value("${spring.app.JwtSecret}")
    private String jwtSecret;
    @Value("${spring.app.jwtCookieName}")
    private String jwtCookieName;

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        String jwt=generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie cookie=ResponseCookie.from(jwtCookieName,jwt)
                .maxAge(24*60*60)
                .path("/api")
                .httpOnly(false)
                .build();
        return cookie;

    }
    public String generateTokenFromUsername(String userName){
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime()+jwtExpirationInMilliseconds)))
                .signWith((SecretKey)key())
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty : {}", e.getMessage());
        }
        return false;
    }
}
