package autoria.mapper;

import autoria.dto.CarPostingDTO;
import autoria.entity.CarPosting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CarPostingMapper {

    CarPosting convertFromDto(CarPostingDTO dto);

    CarPostingDTO convertToDto(CarPosting carPosting);
}
