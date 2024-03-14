package autoria.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarPostingInfo {
    private Long totalViews;
    private Long dailyViews;
    private Long weeklyViews;
    private Long monthlyViews;
    private Double averagePriceByRegion;
    private Double averagePriceUkraine;

}
