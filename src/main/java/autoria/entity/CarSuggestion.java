package autoria.entity;

import autoria.entity.enums.CarModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "cars_suggestions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CarSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Type cannot be empty")
    private String type;

    @NotEmpty(message = "Brand cannot be empty")
    private String brand;

    private String model;

    private String notes;

    private String name;

    private String email;
}
