package autoria.controller;

import autoria.entity.CarBrand;
import autoria.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
