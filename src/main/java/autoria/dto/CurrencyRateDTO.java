package autoria.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyRateDTO {
    private String ccy;
    private String base_ccy;
    private String buy;
    private String sale;
}
