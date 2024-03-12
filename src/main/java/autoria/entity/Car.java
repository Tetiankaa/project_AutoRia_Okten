package autoria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cars")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private CarBrand brand;

    private CarModel model;

    private int year;

    private double mileage;

    private String bodyType;

    private String city;

    private String region;

    private String description;

    private String color;

    private Long price;


}
