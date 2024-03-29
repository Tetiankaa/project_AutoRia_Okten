package autoria.repository;

import autoria.entity.Car;
import autoria.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CurrencyRateDAO extends JpaRepository<CurrencyRate, Long> {

    Optional<CurrencyRate> findByCcy(String currency);

}
