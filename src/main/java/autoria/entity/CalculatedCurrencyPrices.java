package autoria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calculated_currency_rates")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CalculatedCurrencyPrices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private Double UAH;

    private Double USD;

    private Double rateUSD;

    private Double EUR;

    private Double rateEUR;

}
