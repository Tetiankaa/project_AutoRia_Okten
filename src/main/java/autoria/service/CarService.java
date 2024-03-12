package autoria.service;

import autoria.entity.CarBrand;
import autoria.entity.CarModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CarService {

    public ResponseEntity<List<String>> getBrands(){
        List<String> brands = Arrays.stream(CarBrand.values()).map(CarBrand::getName).toList();
        return ResponseEntity.ok(brands);
    }

    public ResponseEntity<List<String>> getModels() {
        List<String> models = Arrays.stream(CarModel.values()).map(CarModel::getName).toList();
        return ResponseEntity.ok(models);

    }
}
