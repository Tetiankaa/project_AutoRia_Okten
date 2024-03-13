package autoria.service;

import autoria.dto.CarDTO;
import autoria.dto.CarSuggestionDTO;
import autoria.entity.Car;
import autoria.entity.CarAd;
import autoria.entity.CarSuggestion;
import autoria.entity.User;
import autoria.entity.enums.*;
import autoria.entity.enums.Currency;
import autoria.exception.CustomException;
import autoria.handler.ErrorHandler;
import autoria.mapper.CarMapper;
import autoria.mapper.CarSuggestionMapper;
import autoria.repository.CarAdDAO;
import autoria.repository.CarDAO;
import autoria.repository.CarSuggestionDAO;
import autoria.repository.UserDAO;
import autoria.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CarService {
    private final static String DIRECTORY_FOR_PHOTOS = "CarPhotos";

    private final CarMapper carMapper;
    private final CarDAO carDAO;
    private final CarAdDAO carAdDAO;
    private final UserDAO userDAO;
    private final AuthenticationUtil authenticationUtil;
    private final CarSuggestionDAO carSuggestionDAO;
    private final CarSuggestionMapper carSuggestionMapper;


    public ResponseEntity<List<String>> getBrands(){
        List<String> brands = Arrays.stream(CarBrand.values()).map(CarBrand::getName).toList();
        return ResponseEntity.ok(brands);
    }

    public ResponseEntity<List<String>> getModels() {
        List<String> models = Arrays.stream(CarModel.values()).map(CarModel::getName).toList();
        return ResponseEntity.ok(models);

    }

    public ResponseEntity<List<String>> getCurrencies() {
        List<String> currencies = Arrays.stream(Currency.values()).map(Currency::getName).toList();
        return ResponseEntity.ok(currencies);
    }

    public ResponseEntity<?> saveCar(CarDTO carDTO) throws IOException, CustomException {

            User user = authenticationUtil.getAuthenticatedUser();

            if (!user.getRole().equals(Roles.SELLER)){
                user.setRole(Roles.SELLER);
                user.setAccount(Account.BASIC);
                userDAO.save(user);
            }else {
                if (user.getAccount().equals(Account.BASIC) && user.getAds().size() == 1){
                    throw new CustomException("Basic accounts can only create 1 ad.");
                }
            }
            String photoName = storePhoto(carDTO.getPhoto());

            Car car = carMapper.convertToCar(carDTO);
            car.setPhotoName(photoName);

            Car savedCar = carDAO.save(car);

            CarAd ad = new CarAd();
            ad.setDate(new Date(System.currentTimeMillis()));
            ad.setCar(savedCar);
            ad.setUser(user);

            carAdDAO.save(ad);

            return ResponseEntity.accepted().body(carMapper.convertToDto(savedCar));

    }

    public ResponseEntity<String> createCarRequest(CarSuggestionDTO carSuggestionDTO) {
        CarSuggestion suggestion = carSuggestionMapper.convertFromDto(carSuggestionDTO);
        carSuggestionDAO.save(suggestion);
        // TODO implement sending email
        return ResponseEntity.accepted().body("Request has been successfully sent");
    }

    private String storePhoto(MultipartFile photo) throws IOException {

         String filename = photo.getOriginalFilename();

         File targetDirectory  = createDirectoryIfNotExist();

         File fileForPhotos = new File(targetDirectory + File.separator + filename);
         photo.transferTo(fileForPhotos);

         return filename;

    }

    private File createDirectoryIfNotExist() throws IOException {
        File file = new File(System.getProperty("user.home") + File.separator + DIRECTORY_FOR_PHOTOS);

        if (!file.exists() && !file.mkdir()){
            throw new IOException("Failed to create directory: " + file.getAbsolutePath());
        }

        return file;
    }

}
