package autoria.service;

import autoria.dto.CarDTO;
import autoria.dto.CarSuggestionDTO;
import autoria.entity.Car;
import autoria.entity.CarPosting;
import autoria.entity.CarSuggestion;
import autoria.entity.User;
import autoria.entity.enums.*;
import autoria.entity.enums.Currency;
import autoria.exception.CustomException;
import autoria.mapper.CarMapper;
import autoria.mapper.CarPostingMapper;
import autoria.mapper.CarSuggestionMapper;
import autoria.repository.CarPostingDAO;
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
    private final CarPostingDAO carPostingDAO;
    private final UserDAO userDAO;
    private final AuthenticationUtil authenticationUtil;
    private final CarSuggestionDAO carSuggestionDAO;
    private final CarSuggestionMapper carSuggestionMapper;
    private final ProfanityFilterService profanityService;
    private final CarPostingMapper carPostingMapper;


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
                if (user.getAccount().equals(Account.BASIC) && user.getPostings().size() == 1){
                    throw new CustomException("Basic accounts can only create 1 posting.");
                }
            }
            String photoName = storePhoto(carDTO.getPhoto());

            Car car = carMapper.convertToCar(carDTO);
            car.setPhotoName(photoName);

             carDAO.save(car);

            CarPosting carPosting = new CarPosting();

        boolean presentProfanity = profanityService.containsProfanity(car.getDescription());

        if (!presentProfanity){
            carPosting.setStatus(CarPostingStatus.ACTIVE);
        }else {
            carPosting.setStatus(CarPostingStatus.NOT_ACTIVE);
        }


            carPosting.setDate(new Date(System.currentTimeMillis()));
            carPosting.setCar(car);
            carPosting.setUser(user);


            carPostingDAO.save(carPosting);

        return ResponseEntity.accepted().body(carPostingMapper.convertToDto(carPosting));

    }

    public ResponseEntity<String> createCarRequest(CarSuggestionDTO carSuggestionDTO) {
        CarSuggestion suggestion = carSuggestionMapper.convertFromDto(carSuggestionDTO);
        carSuggestionDAO.save(suggestion);
        // TODO implement sending email
        return ResponseEntity.accepted().body("Request has been successfully sent");
    }

    public ResponseEntity<?> updateCarAfterProfanity(CarDTO carDTO) {

        Car car = carMapper.convertToCar(carDTO);
    }

    public List<CarDTO> getAll(){
      return  carDAO.findAll().stream().map(carMapper::convertToDto).toList();

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
