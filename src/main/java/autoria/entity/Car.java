package autoria.entity;

import autoria.entity.enums.CarBrand;
import autoria.entity.enums.CarModel;
import autoria.entity.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

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

    @NotEmpty(message = "Type cannot be empty")
    private String type;

    @Enumerated(EnumType.STRING)
    private CarBrand brand;

    @Enumerated(EnumType.STRING)
    private CarModel model;

    @NotNull(message = "Year cannot be empty")
    @Min(value = 0, message = "Year cannot be negative")
    private Integer year;

    @NotNull(message = "Mileage cannot be empty")
    @Min(value = 0, message = "Mileage cannot be negative")
    private Long mileage;

    @NotEmpty(message = "Body type cannot be empty")
    @Size(min = 2, max = 50, message = "Body type must be between 2 and 50 characters long")
    private String bodyType;

    @NotEmpty(message = "Region cannot be empty")
    @Size(min = 3, message = "Region must be minimum 3 characters long")
    private String region;

    @NotEmpty(message = "City cannot be empty")
    @Size(min = 3, message = "City must be minimum 3 characters long")
    private String city;

    @NotEmpty(message = "Color cannot be empty")
    @Size(min = 3, message = "Color must be minimum 3 characters long")
    private String color;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "car")
    private CalculatedCurrencyPrices currencyPrices;

    @NotNull(message = "Price cannot be empty")
    @Min(value = 0, message = "Price should not be negative")
    private Double enteredPrice;

    private String description;

    @NotEmpty(message = "Photo cannot be empty")
    private String photoName;


    public void setYear(int year){
        if (year > LocalDateTime.now().getYear()){
            throw new IllegalArgumentException("Year cannot be in the future");
        }
        this.year = year;
    }

}
