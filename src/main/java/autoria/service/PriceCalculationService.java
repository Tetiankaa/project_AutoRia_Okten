package autoria.service;

import autoria.dto.CarDTO;
import autoria.entity.CalculatedCurrencyPrices;
import autoria.entity.Car;
import autoria.entity.enums.CarBrand;
import autoria.exception.CustomException;
import autoria.mapper.CarMapper;
import autoria.repository.CalculatedCurrencyPricesDAO;
import autoria.repository.CarDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
@RequiredArgsConstructor
public class PriceCalculationService {

    private final CarDAO carDAO;
    private final CalculatedCurrencyPricesDAO calculatedCurrencyPricesDAO;


    public Double getAverageCarsPrice(CarBrand carBrand)  {
        List<Car> cars = carDAO
                .findAll()
                .stream()
                .filter(car -> car.getBrand().equals(carBrand))
                .toList();

        List<Double> uahPrices = getUahPricesForCars(cars);

        return uahPrices
                .isEmpty()
                    ? 0.0
                    : uahPrices
                        .stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);
    }

    public Double getAverageCarsPriceByRegion(String region, CarBrand carBrand) {
        List<Car> cars = carDAO
                .findAll()
                .stream()
                .filter(car -> car.getRegion().equals(region) && car.getBrand().equals(carBrand))
                .toList();

        List<Double> uahPrices = getUahPricesForCars(cars);

        return uahPrices
                .isEmpty()
                    ? 0.0
                    : uahPrices
                        .stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);
    }

    private List<Double> getUahPricesForCars(List<Car> cars) {
        return cars.stream()
                .map(car -> calculatedCurrencyPricesDAO.findByCar(car).map(CalculatedCurrencyPrices::getUAH).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
