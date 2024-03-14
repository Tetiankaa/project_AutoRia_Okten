package autoria.repository;

import autoria.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarDAO extends JpaRepository<Car,Long> {
    Optional<List<Car>> findByRegion(String region);
}
