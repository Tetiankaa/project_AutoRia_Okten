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

    @Column(name = "rate_usd")
    private Double rateUSD;

    private Double EUR;

    @Column(name = "rate_eur")
    private Double rateEUR;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

}
