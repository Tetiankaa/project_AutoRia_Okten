package autoria.service;


import autoria.dto.CarDTO;
import autoria.entity.User;
import autoria.entity.enums.Account;
import autoria.mapper.UserMapper;
import autoria.repository.UserDAO;
import autoria.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final AuthenticationUtil authenticationUtil;

    public ResponseEntity<String> upgrade(){

        User user = authenticationUtil.getAuthenticatedUser();
        user.setAccount(Account.PREMIUM);
        userDAO.save(user);

        return ResponseEntity.ok("Account was successfully upgraded to Premium.");
    }

}
