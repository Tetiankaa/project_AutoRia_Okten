package autoria.controller;

import autoria.dto.AuthRequest;
import autoria.dto.AuthResponse;
import autoria.dto.RefreshRequest;
import autoria.dto.UserDTO;
import autoria.exception.CustomException;
import autoria.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-user")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid UserDTO userDTO){
        return authService.registerUser(userDTO);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody @Valid UserDTO userDTO){
        return authService.registerAdmin(userDTO);
    }

    @PostMapping("/register-manager")
    public ResponseEntity<String> registerManager(@RequestBody @Valid UserDTO userDTO) throws CustomException {
        return authService.registerManager(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        return authService.loginUser(authRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request){
        return authService.refresh(request);
    }
}
