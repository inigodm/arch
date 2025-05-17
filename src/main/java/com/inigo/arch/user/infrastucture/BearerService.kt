package com.inigo.arch.user.infrastucture;

import com.inigo.arch.user.model.LogedInUser;
import com.inigo.arch.user.model.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.UUID;

@Component
public class BearerService implements TokenService {
    private final String JWT_SECRET = """
    random string dfsdfenrñjweugb
    must be a long string in order to 
    have more than 256 bytes, so I think that I should 
    write some more characters here: 
    lets see: 1, 2, 3
""";

    @Override
    public String generateToken(String username, String email, UUID id, int userRole) {
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        String compactTokenString = Jwts.builder()
                .claim("jti", UUID.randomUUID().toString())
                .claim("id", id)
                .claim("sub", username)
                .claim("email", email)
                .claim("userRole", userRole)
                .setExpiration(null)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + compactTokenString;
    }

    @Override
    public LogedInUser parseToken(String bearer) {
        String token = bearer.replace("Bearer ", "");
        byte[] secretBytes = JWT_SECRET.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token); //Jwt, plaintext....
        String username = jwsClaims.getBody().getSubject();
        UUID userId = UUID.fromString(jwsClaims.getBody().get("id", String.class));
        String email = jwsClaims.getBody().get("email", String.class);
        int userRole = jwsClaims.getBody().get("userRole", Integer.class);
        return new LogedInUser(username, email, userId, userRole);
    }
}
