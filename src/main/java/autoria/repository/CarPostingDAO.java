package autoria.repository;

import autoria.entity.CarPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPostingDAO extends JpaRepository<CarPosting, Long> {
}
