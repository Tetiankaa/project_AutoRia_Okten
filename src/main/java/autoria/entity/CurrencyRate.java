package autoria.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "currency_rates")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private String ccy;
    private String base_ccy;
    private String buy;
    private String sale;
}
