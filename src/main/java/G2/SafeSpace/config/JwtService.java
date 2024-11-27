/**
 * Service for managing JSON Web Tokens (JWT) in the application.
 * Handles token creation, validation, and extraction of claims and subject information.
 */
package G2.SafeSpace.config;

import G2.SafeSpace.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    /**
     * Generates a JWT token for the given user with additional claims.
     *
     * @param user         the {@link User} for whom the token is being generated.
     * @param extraClaims  a {@link Map} containing additional claims to include in the token.
     * @return the generated JWT as a {@link String}.
     */
    public String generateToken(User user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_MINUTES * 60000);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates the secret key used for signing and verifying JWT tokens.
     *
     * @return the generated {@link Key}.
     */
    private Key generateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (subject) from the given JWT.
     *
     * @param jwt the JWT token from which the username is extracted.
     * @return the username as a {@link String}.
     */
    public String extractUsername(String jwt) {

        return extractedAllClaims(jwt).getSubject();
    }

    /**
     * Extracts all claims from the given JWT.
     *
     * @param jwt the JWT token from which claims are extracted.
     * @return a {@link Claims} object containing the claims, or {@code null} if the token is invalid.
     */
    private Claims extractedAllClaims(String jwt) {
        try{
            return Jwts.parser().setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
