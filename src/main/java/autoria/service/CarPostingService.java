package autoria.service;

import autoria.dto.CarDTO;
import autoria.dto.CarPostingDTO;
import autoria.dto.CarPostingInfo;
import autoria.entity.Car;
import autoria.entity.CarPosting;
import autoria.entity.CarPostingView;
import autoria.entity.User;
import autoria.entity.enums.Account;
import autoria.entity.enums.CarPostingStatus;
import autoria.exception.CustomException;
import autoria.mapper.CarMapper;
import autoria.mapper.CarPostingMapper;
import autoria.repository.CarDAO;
import autoria.repository.CarPostingDAO;
import autoria.repository.CarPostingViewDAO;
import autoria.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarPostingService {

    private final CarPostingDAO carPostingDAO;
    private final CarPostingMapper postingMapper;
    private final CarPostingViewDAO carPostingViewDAO;

    private final AuthenticationUtil authenticationUtil;

    private final PriceCalculationService priceCalculationService;


    public CarPostingDTO createPosting(Car car, User user, boolean isProfanityPresent){

        CarPosting carPosting = new CarPosting();

        carPosting.setDate(new Date(System.currentTimeMillis()));
        carPosting.setCar(car);
        carPosting.setUser(user);
        carPosting.setProfanityEdits(0);
        carPosting.setStatus(isProfanityPresent ? CarPostingStatus.NOT_ACTIVE : CarPostingStatus.ACTIVE);

        CarPosting savedPosting = carPostingDAO.save(carPosting);

        return postingMapper.convertToDto(savedPosting);
    }

    public ResponseEntity<CarPostingDTO> getById(Long id) throws CustomException {

        CarPosting posting = carPostingDAO
                .findById(id)
                .orElseThrow(() -> new CustomException("Posting with id " + id + " was not found."));

        return ResponseEntity.ok(postingMapper.convertToDto(posting));
    }

    public ResponseEntity<List<CarPostingDTO>> getAll(){

        List<CarPostingDTO> allPostings =
                carPostingDAO
                        .findAll()
                        .stream()
                        .map(postingMapper::convertToDto)
                        .toList();

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

        Double averagePriceUkraine = priceCalculationService.getAverageCarsPrice();

        String region = carPosting.getCar().getRegion();
        Double averagePriceByRegion = priceCalculationService.getAverageCarsPriceByRegion(region);

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



}
