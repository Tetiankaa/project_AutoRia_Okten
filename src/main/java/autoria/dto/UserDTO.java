package autoria.dto;

import autoria.entity.Roles;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String password;

    private Roles role;

    private String refreshToken;

}
