package autoria.repository;

import autoria.entity.User;
import autoria.entity.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findUserByRole(Roles role);

}
