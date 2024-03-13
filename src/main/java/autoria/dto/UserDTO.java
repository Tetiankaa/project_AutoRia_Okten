package autoria.dto;

import autoria.entity.enums.Roles;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private Roles role;

    private String refreshToken;

}
