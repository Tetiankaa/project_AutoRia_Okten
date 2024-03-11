package autoria.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.signinKey}")
    private String secreteKey;

    public String generateAccessToken(UserDetails userDetails){
        return Jwts.builder()
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigninKey(){
        byte[] decodedValue = Base64.getDecoder().decode(secreteKey);
        return Keys.hmacShaKeyFor(decodedValue);
    }
}
