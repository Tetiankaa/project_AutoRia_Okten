package autoria.controller;

import autoria.dto.CarPostingDTO;
import autoria.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;


    @PatchMapping("/upgrade")
    public ResponseEntity<String> upgradeForPremium(){
        return userService.upgrade();
    }

    @GetMapping("/postings")
    public ResponseEntity<List<CarPostingDTO>> getUserPostings(){
        return userService.getPostings();
    }



}
