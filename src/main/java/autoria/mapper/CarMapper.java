package autoria.mapper;

import autoria.dto.CarDTO;
import autoria.entity.Car;
import org.mapstruct.*;

@Mapper
public interface CarMapper {

    Car convertToCar(CarDTO carDTO);

    CarDTO convertToDto(Car car);


}
