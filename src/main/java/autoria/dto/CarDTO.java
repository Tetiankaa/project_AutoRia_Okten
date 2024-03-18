package autoria.dto;

import autoria.entity.CalculatedCurrencyPrices;
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

    private CalculatedCurrencyPrices currencyPrices;

    private String city;

    private String color;

    private Currency currency;

    private Double enteredPrice;

    private String description;

    private MultipartFile photo;

    private String photoName;
}
