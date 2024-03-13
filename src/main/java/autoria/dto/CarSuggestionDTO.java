package autoria.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarSuggestionDTO {

    private Long id;

    private String type;

    private String brand;

    private String model;

    private String notes;

    private String name;

    private String email;
}
