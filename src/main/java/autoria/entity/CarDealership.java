package autoria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_dealerships")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CarDealership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String city;

    private String region;

    private String email;

    private String phone;

    private String workingHours;
}
