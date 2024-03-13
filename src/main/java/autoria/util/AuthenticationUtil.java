package autoria.util;

import autoria.entity.User;
import autoria.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthenticationUtil {

    private final UserDAO userDAO;

    public User getAuthenticatedUser(){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        return userDAO.findByEmail(username).orElseThrow();
    }
}
