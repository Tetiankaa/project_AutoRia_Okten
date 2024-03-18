package autoria.controller;

import autoria.dto.CarDTO;
import autoria.dto.CarPostingDTO;
import autoria.dto.CarPostingInfo;
import autoria.exception.CustomException;
import autoria.service.CarPostingService;
import autoria.service.ProfanityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/postings")
public class CarPostingController {

    private final CarPostingService carPostingService;
    private final ProfanityService profanityService;

    @GetMapping("/{id}")
    public ResponseEntity<CarPostingDTO> getById(@PathVariable Long id) throws CustomException {
        return carPostingService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<CarPostingDTO>> getAll(){
        return carPostingService.getAll();
    }

    @PostMapping("/{id}/views")
    public ResponseEntity<Void> saveView(@PathVariable Long id) throws CustomException {
        return carPostingService.saveView(id);
    }


    @GetMapping("/{id}/info")
    public ResponseEntity<CarPostingInfo> getCarPostingInfo(@PathVariable Long id) throws CustomException {
        return carPostingService.getCarPostingInfo(id);
    }

    @PatchMapping("/{id}/update-after-profanity")
    public ResponseEntity<?> updateCarAfterProfanity(@Valid @ModelAttribute CarDTO carDTO, @PathVariable Long id) throws CustomException {
        return profanityService.updateCarAfterProfanity(id ,carDTO);
    }

}
