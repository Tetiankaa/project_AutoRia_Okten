package autoria.controller;

import autoria.dto.CarDTO;
import autoria.dto.CarPostingDTO;
import autoria.dto.CarSuggestionDTO;
import autoria.dto.CurrencyRateDTO;
import autoria.entity.CurrencyRate;
import autoria.exception.CustomException;
import autoria.service.CarService;
import autoria.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands(){
       return carService.getBrands();
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> getModels(){
        return carService.getModels();
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getCurrencies(){
        return carService.getCurrencies();
    }

    @PostMapping("/save")
    public ResponseEntity<CarPostingDTO> saveCar(@Valid @ModelAttribute CarDTO carDTO) throws IOException, CustomException {
        return carService.saveCar(carDTO);
    }

    @PostMapping("/suggestion")
    public ResponseEntity<String> suggestCar(@RequestBody CarSuggestionDTO carSuggestionDTO){
        return carService.createCarRequest(carSuggestionDTO);
    }

}
