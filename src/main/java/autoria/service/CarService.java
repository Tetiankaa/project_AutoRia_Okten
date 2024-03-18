package autoria.service;

import autoria.dto.CarDTO;
import autoria.dto.CarPostingDTO;
import autoria.dto.CarSuggestionDTO;
import autoria.entity.*;
import autoria.entity.enums.*;
import autoria.entity.enums.Currency;
import autoria.exception.CustomException;
import autoria.filter.ProfanityFilter;
import autoria.mapper.CarMapper;
import autoria.mapper.CarSuggestionMapper;
import autoria.repository.*;
import autoria.scheduler.CurrencyRateScheduler;
import autoria.util.AuthenticationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CarService {

    private final static String DIRECTORY_FOR_PHOTOS = "CarPhotos";

    @Value("${spring.mail.username}")
    private String companyEmail;


    private final CarMapper carMapper;
    private final CarDAO carDAO;
    private final UserDAO userDAO;
    private final AuthenticationUtil authenticationUtil;
    private final CarSuggestionDAO carSuggestionDAO;
    private final CarSuggestionMapper carSuggestionMapper;
    private final ProfanityFilter profanityService;
    private final CarPostingService carPostingService;
    private final MailService mailService;
    private final CurrencyRateDAO currencyRateDAO;
    private final CurrencyService currencyService;
    private final CalculatedCurrencyPricesDAO calculatedCurrencyDAO;
    private final CurrencyRateScheduler currencyRateScheduler;


    @PostConstruct
    public void updateCurrencyRatesOnStartup() throws IOException {
        currencyRateScheduler.updateCurrencyRates();
    }
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

    public ResponseEntity<CarPostingDTO> saveCar(CarDTO carDTO) throws IOException, CustomException {

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

        Car savedCar = carDAO.save(car);

       CurrencyRate currencyEur = currencyRateDAO.findByCcy("EUR").orElseThrow();
       CurrencyRate currencyUsd = currencyRateDAO.findByCcy("USD").orElseThrow();

        Double saleEur = Double.parseDouble(currencyEur.getSale());
        Double saleUsd = Double.parseDouble(currencyUsd.getSale());
        Double buyEur = Double.parseDouble(currencyEur.getBuy());
        Double buyUsd = Double.parseDouble(currencyUsd.getBuy());

        CalculatedCurrencyPrices calculatedCurrencyPrices = currencyService.calculateCurrencyPrices(savedCar, saleEur, saleUsd, buyEur, buyUsd);
        calculatedCurrencyPrices.setCar(savedCar);
        calculatedCurrencyDAO.save(calculatedCurrencyPrices);

        boolean containsProfanity = profanityService.containsProfanity(car.getDescription());

            CarPostingDTO posting = carPostingService.createPosting(car, user, containsProfanity);

             return ResponseEntity.accepted().body(posting);

    }

    public ResponseEntity<String> createCarRequest(CarSuggestionDTO carSuggestionDTO) {
        CarSuggestion suggestion = carSuggestionMapper.convertFromDto(carSuggestionDTO);
        carSuggestionDAO.save(suggestion);

        mailService.sendEmail(companyEmail,"Received new car suggestion. Brand: " + suggestion.getBrand() + ", model: " + suggestion.getModel(),"Car suggestion");

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
