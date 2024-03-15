package autoria.service;

import autoria.dto.CarDTO;
import autoria.entity.Car;
import autoria.entity.CarPosting;
import autoria.entity.User;
import autoria.entity.enums.CarPostingStatus;
import autoria.entity.enums.Roles;
import autoria.exception.CustomException;
import autoria.filter.ProfanityFilter;
import autoria.mapper.CarMapper;
import autoria.mapper.CarPostingMapper;
import autoria.repository.CarDAO;
import autoria.repository.CarPostingDAO;
import autoria.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfanityService {


    private final CarPostingDAO carPostingDAO;
    private final CarPostingMapper carPostingMapper;
    private final ProfanityFilter profanityFilter;
    private final CarDAO carDAO;
    private final CarMapper carMapper;
    private final MailService mailService;
    private final UserDAO userDAO;


    public ResponseEntity<?> updateCarAfterProfanity(Long postingId ,CarDTO carDTO) throws CustomException {
        CarPosting posting = carPostingDAO.findById(postingId).orElseThrow(() -> new CustomException("Posting with id " + postingId + " was not found."));

        Long carId = posting.getCar().getId();

        Car car = carDAO.findById(carId).orElseThrow();

        carMapper.patchCar(car,carDTO);

        carDAO.save(car);

        boolean isProfanityPresent = profanityFilter.containsProfanity(carDTO.getDescription());

        posting.setStatus(isProfanityPresent ? CarPostingStatus.NOT_ACTIVE : CarPostingStatus.ACTIVE);

        posting.setProfanityEdits(posting.getProfanityEdits() + 1);

        CarPosting editedPosting = carPostingDAO.save(posting);


        if (isProfanityPresent && editedPosting.getProfanityEdits() == 3){
            Optional<User> manager = userDAO.findUserByRole(Roles.MANAGER);

            if (manager.isPresent()){
                String subject = "Detected posting with profanity language";

                String  message = "Posting **ID**: " + editedPosting.getId();

                mailService.sendEmail(manager.get().getEmail(),message, subject);

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Car description update was rejected due to profanity. Was reached the maximum allowed edits (3).");
            }


        }

        return ResponseEntity.ok(carPostingMapper.convertToDto(editedPosting));

    }
}
