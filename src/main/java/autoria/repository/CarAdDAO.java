package autoria.repository;

import autoria.entity.CarAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarAdDAO extends JpaRepository<CarAd, Long> {
}
