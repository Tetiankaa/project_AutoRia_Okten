package autoria.repository;

import autoria.entity.CarSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarSuggestionDAO extends JpaRepository<CarSuggestion, Long> {
}
