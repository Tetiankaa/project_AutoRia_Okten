package autoria.dto;

import autoria.entity.enums.CarBrand;
import autoria.entity.enums.CarModel;
import autoria.entity.enums.Currency;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class CarDTO {

    private Long id;

    private String type;

    private CarBrand brand;

    private CarModel model;

    private Integer year;

    private Long mileage;

    private String bodyType;

    private String region;

    private String city;

    private String color;

    private Currency currency;

    private Double price;

    private String description;

    private MultipartFile photo; // TODO make set of photos

    private String photoName;
}
