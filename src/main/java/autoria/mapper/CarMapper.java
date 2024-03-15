package autoria.mapper;

import autoria.dto.CarDTO;
import autoria.entity.Car;
import org.mapstruct.*;

@Mapper
public interface CarMapper {

    Car convertToCar(CarDTO carDTO);

    CarDTO convertToDto(Car car);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",ignore = true)
    void patchCar(@MappingTarget Car car, CarDTO source);


}
