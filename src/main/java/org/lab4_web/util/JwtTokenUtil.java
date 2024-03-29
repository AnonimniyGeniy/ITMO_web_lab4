package org.lab4_web.util;

import io.jsonwebtoken.*;
import org.lab4_web.model.User;

import javax.ejb.Singleton;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class JwtTokenUtil {

    final private String secret = "mysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecret";
    final private long expiration = 86400000;


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public boolean validateToken(String reqToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(reqToken);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

}