package autoria.controller;

import autoria.dto.CarDTO;
import autoria.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;


    @PatchMapping("/upgrade")
    public ResponseEntity<String> upgradeForPremium(){
        return userService.upgrade();
    }
}
