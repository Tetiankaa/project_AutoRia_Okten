package autoria.repository;

import autoria.entity.CalculatedCurrencyPrices;
import autoria.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalculatedCurrencyPricesDAO  extends JpaRepository<CalculatedCurrencyPrices, Long> {

    Optional<CalculatedCurrencyPrices> findByCar(Car car);


}
