package autoria.service;


import autoria.dto.CarDTO;
import autoria.dto.CarPostingDTO;
import autoria.entity.CarPosting;
import autoria.entity.User;
import autoria.entity.enums.Account;
import autoria.mapper.CarPostingMapper;
import autoria.mapper.UserMapper;
import autoria.repository.UserDAO;
import autoria.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final AuthenticationUtil authenticationUtil;
    private final CarPostingMapper carPostingMapper;

    public ResponseEntity<String> upgrade(){

        User user = authenticationUtil.getAuthenticatedUser();

        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        }

        user.setAccount(Account.PREMIUM);

        userDAO.save(user);

        return ResponseEntity.ok("Account was successfully upgraded to Premium.");
    }

    public ResponseEntity<List<CarPostingDTO>> getPostings() {
        User user = authenticationUtil.getAuthenticatedUser();
        List<CarPosting> userPostings = user.getPostings();

        return ResponseEntity.ok(userPostings.stream().map(carPostingMapper::convertToDto).toList());
    }
}
