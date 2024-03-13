package autoria.mapper;

import autoria.dto.CarSuggestionDTO;
import autoria.entity.CarSuggestion;
import org.mapstruct.Mapper;

@Mapper
public interface CarSuggestionMapper {

    CarSuggestionDTO convertToDto(CarSuggestion carSuggestion);

    CarSuggestion convertFromDto(CarSuggestionDTO carSuggestionDTO);

}
