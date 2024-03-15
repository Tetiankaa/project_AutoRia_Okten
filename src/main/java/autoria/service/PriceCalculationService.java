package autoria.service;

import autoria.dto.CarDTO;
import autoria.exception.CustomException;
import autoria.mapper.CarMapper;
import autoria.repository.CarDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceCalculationService {

    private final CarMapper carMapper;
    private final CarDAO carDAO;

    public Double getAverageCarsPrice() throws CustomException {

        return getAll()
                .stream()
                .mapToDouble(CarDTO::getPrice) //TODO check for the currency and convert to the single currency
                .average()
                .orElseThrow(()-> new CustomException("Cannot calculate average cars price data."));
    }

    public Double getAverageCarsPriceByRegion(String region) throws CustomException {

        return getAll()
                .stream()
                .filter(car -> car.getRegion().equals(region))
                .mapToDouble(CarDTO::getPrice)
                .average()
                .orElseThrow(()-> new CustomException("Cannot calculate average cars price by " + region + " region"));
    }

    private List<CarDTO> getAll(){
        return  carDAO.findAll().stream().map(carMapper::convertToDto).toList();

    }
}
