package autoria.service;

import autoria.dto.AuthRequest;
import autoria.dto.AuthResponse;
import autoria.dto.RefreshRequest;
import autoria.dto.UserDTO;
import autoria.entity.Roles;
import autoria.entity.User;
import autoria.exception.JwtAuthException;
import autoria.mapper.UserMapper;
import autoria.repository.UserDAO;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDAO userDAO;
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
   


    public ResponseEntity<AuthResponse> registerUser(UserDTO userData){
        User user = mapper.convertToUser(userData);
        user.setRole(Roles.BUYER);
        user.setPassword(passwordEncoder.encode(userData.getPassword()));

        return  ResponseEntity.ok(generateTokensAndResponse(user));

    }

    public ResponseEntity<AuthResponse> loginUser(AuthRequest loginData){
        UsernamePasswordAuthenticationToken unauthenticatedUser = UsernamePasswordAuthenticationToken.unauthenticated(loginData.getEmail(), loginData.getPassword());
        authenticationManager.authenticate(unauthenticatedUser);

        User user = userDAO.findByEmail(loginData.getEmail()).orElseThrow();

        return ResponseEntity.ok(generateTokensAndResponse(user));
    }

    public ResponseEntity<AuthResponse> refresh(RefreshRequest request){

    try {

        String refresh =  request.getRefreshToken();
        Date refreshTokenExpirationDate =  jwtService.getExpiration(refresh);

        System.out.println("refresh: " + refreshTokenExpirationDate);

      if (refreshTokenExpirationDate.before(new Date())){
          throw new JwtException("Refresh token has expired");
      }

        String username = jwtService.extractUsername(refresh);

        User user = userDAO.findByEmail(username).orElseThrow();
        AuthResponse authResponse = new AuthResponse();

        if (refresh.equals(user.getRefreshToken())){

            String accessToken = jwtService.generateAccessToken(user);
            Date accessTokenExpirationDate  = jwtService.getExpiration(accessToken);

            System.out.println("access: " + accessTokenExpirationDate);

            authResponse.setAccessToken(accessToken);

            if (refreshTokenExpirationDate.before(accessTokenExpirationDate)){
                String refreshToken = jwtService.generateRefreshToken(user);
                user.setRefreshToken(refreshToken);
                userDAO.save(user);
                authResponse.setRefreshToken(refreshToken);
            }else {
                authResponse.setRefreshToken(refresh);
            }
        }

        return ResponseEntity.ok(authResponse);
    }catch (JwtException exception){
        throw new JwtAuthException(exception.getMessage(), exception);
    }
    
    }


    private AuthResponse generateTokensAndResponse(User user){
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);

        userDAO.save(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);

        return authResponse;
    }
}
