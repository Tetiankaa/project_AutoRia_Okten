package autoria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long")
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String name;

    @NotEmpty(message = "Surname may not be empty")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters long")
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String surname;

    @NotEmpty(message = "Email may not be empty")
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "Email address must be in a valid format (Example: user12@example.com)")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Phone number may not be empty")
    @Pattern(regexp = "^\\+?3?8?(0[\\s-]\\d{2}[\\s-]\\d{3}[\\s-]\\d{2}[\\s-]\\d{2})$", message = "Phone number must be in a valid format (Example: +380679638574)")
    private String phone;

    @NotEmpty(message = "Password may not be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*)(?=.*[@$!%*#?&])[A-Za-z@$!%*#?&]{8,}$", message = "Password must have minimum 8 characters, at least one letter, one number and one special character")
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private String refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));

        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
