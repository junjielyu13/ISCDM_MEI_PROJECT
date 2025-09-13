package filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

/**
 *
 * @author alumne
 */
public class AuthFilter {

    private static final Key key = Keys.hmacShaKeyFor("miClaveSecreta12345678901234567890_01234567890123456789012345678901".getBytes());

    public static String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 3600_000);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public static String getEmailFromToken(String token) {
        return parseToken(token).getBody().getSubject();
    }
}
