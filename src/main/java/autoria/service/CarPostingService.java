package autoria.service;

import autoria.dto.CarDTO;
import autoria.dto.CarPostingDTO;
import autoria.dto.CarPostingInfo;
import autoria.entity.CarPosting;
import autoria.entity.CarPostingView;
import autoria.entity.User;
import autoria.entity.enums.Account;
import autoria.exception.CustomException;
import autoria.mapper.CarPostingMapper;
import autoria.repository.CarPostingDAO;
import autoria.repository.CarPostingViewDAO;
import autoria.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarPostingService {

    private final CarPostingDAO carPostingDAO;
    private final CarPostingMapper postingMapper;

    private final CarPostingViewDAO carPostingViewDAO;

    private final AuthenticationUtil authenticationUtil;
    private final CarService carService;


    public ResponseEntity<CarPostingDTO> getById(Long id) throws CustomException {
        CarPosting posting = carPostingDAO.findById(id).orElseThrow(() -> new CustomException("Posting with id " + id + " was not found."));
        return ResponseEntity.ok(postingMapper.convertToDto(posting));
    }

    public ResponseEntity<List<CarPostingDTO>> getAll(){
        List<CarPostingDTO> allPostings = carPostingDAO.findAll().stream().map(postingMapper::convertToDto).toList();
        return ResponseEntity.ok(allPostings);
    }

    public ResponseEntity<Void> saveView(Long id) throws CustomException {

        Optional<CarPosting> posting = carPostingDAO.findById(id);

        if (posting.isEmpty()){
            throw new CustomException("Posting with id " + id + " was not found.");
        }
        CarPostingView postingView = new CarPostingView();
        postingView.setViewedAt(LocalDateTime.now());
        postingView.setCarPostingId(id);

        carPostingViewDAO.save(postingView);

        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<CarPostingInfo> getCarPostingInfo(Long postingId) throws CustomException {
        User authenticatedUser = authenticationUtil.getAuthenticatedUser();
        CarPosting carPosting = carPostingDAO.findById(postingId).orElseThrow(() -> new CustomException("Car posting not found"));

        if (!carPosting.getUser().getId().equals(authenticatedUser.getId())){
            throw new CustomException("Unauthorized access to posting views");
        }

        if (authenticatedUser.getAccount().equals(Account.BASIC)){
            throw new CustomException("The posting details is not allowed for sellers with Basic account.");
        }

        Long totalViews = carPostingViewDAO.countByCarPostingId(postingId);
        Long dailyViews = carPostingViewDAO.countByCarPostingIdAndViewedAtBetween(postingId, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        Long weeklyViews = carPostingViewDAO.countByCarPostingIdAndViewedAtBetween(postingId, LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
        Long monthlyViews = carPostingViewDAO.countByCarPostingIdAndViewedAtBetween(postingId, LocalDateTime.now().minusMonths(1), LocalDateTime.now());

        Double averagePriceUkraine = getAverageCarsPrice();

        String region = carPosting.getCar().getRegion();
        Double averagePriceByRegion = getAverageCarsPriceByRegion(region);

        CarPostingInfo info = CarPostingInfo.builder()
                .totalViews(totalViews)
                .dailyViews(dailyViews)
                .weeklyViews(weeklyViews)
                .monthlyViews(monthlyViews)
                .averagePriceUkraine(averagePriceUkraine)
                .averagePriceByRegion(averagePriceByRegion)
                .build();

        return ResponseEntity.ok(info);

    }

    private Double getAverageCarsPrice() throws CustomException {
        List<CarDTO> carServiceAll = carService.getAll();
        return carServiceAll
                .stream()
                .mapToDouble(CarDTO::getPrice) //TODO check for the currency and convert to the single currency
                .average()
                .orElseThrow(()-> new CustomException("Cannot calculate average cars price data."));
    }

    private Double getAverageCarsPriceByRegion(String region) throws CustomException {
        List<CarDTO> carServiceAll = carService.getAll();
        return carServiceAll
                .stream()
                .filter(car -> car.getRegion().equals(region))
                .mapToDouble(CarDTO::getPrice)
                .average()
                .orElseThrow(()-> new CustomException("Cannot calculate average cars price by " + region + " region"));
    }

}
