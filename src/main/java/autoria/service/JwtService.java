package autoria.service;

import autoria.entity.User;
import autoria.repository.UserDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.signinKey}")
    private String secreteKey;

    private final UserDAO userDAO;


    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;
    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 24 * 60 * 60 * 1000;

    public String generateAccessToken(UserDetails userDetails){

        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        return Jwts.builder()
                .setClaims(Map.of("roles",roles))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private  <T> T resolveClaims(Function<Claims,T> resolve, String token){

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return resolve.apply(claims);
    }

    public boolean isTokenExpired(String token){
       return resolveClaims(Claims::getExpiration, token).before(new Date());
    }

    public String extractUsername(String token){
        return resolveClaims(Claims::getSubject, token);
    }

    public Date getExpiration(String token){
        return resolveClaims(Claims::getExpiration, token);
    }
    public boolean isRefreshToken(String token, String username){
        User user = userDAO.findByEmail(username).orElseThrow();
        return token.equals(user.getRefreshToken());
    }

    private Key getSigninKey(){
        byte[] decodedValue = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(decodedValue);
    }
}
