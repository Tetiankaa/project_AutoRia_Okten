package autoria.dto;

import autoria.entity.Car;
import autoria.entity.User;
import autoria.entity.enums.CarPostingStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CarPostingDTO {

    private Long id;

    private Car car;

    private Date date;

    private UserDetails user;

    private CarPostingStatus status;

    private int profanityEdits;
}
