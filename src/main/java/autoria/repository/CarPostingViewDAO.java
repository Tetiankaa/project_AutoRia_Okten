package autoria.repository;

import autoria.entity.CarPostingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CarPostingViewDAO extends JpaRepository<CarPostingView, Long> {

        Long countByCarPostingId(Long postingId);

        Long countByCarPostingIdAndViewedAtBetween(Long postingId, LocalDateTime start, LocalDateTime end);
}
