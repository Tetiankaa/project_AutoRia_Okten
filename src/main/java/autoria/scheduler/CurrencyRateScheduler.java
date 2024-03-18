package autoria.scheduler;

import autoria.dto.CurrencyRateDTO;
import autoria.entity.CalculatedCurrencyPrices;
import autoria.entity.CurrencyRate;
import autoria.entity.enums.Currency;
import autoria.entity.enums.Roles;
import autoria.exception.CustomException;
import autoria.repository.CalculatedCurrencyPricesDAO;
import autoria.repository.CarDAO;
import autoria.repository.CurrencyRateDAO;
import autoria.service.CarService;
import autoria.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrencyRateScheduler {

    private final CurrencyService currencyService;
    private final CurrencyRateDAO currencyRateDAO;
    private final CarDAO carDAO;
    private final CalculatedCurrencyPricesDAO calculatedCurrencyDAO;

    @Scheduled(cron = "0 0 9 * * ?")
    public void updateCurrencyRates() throws IOException {
        Map<String, CurrencyRateDTO> currencyRates  =
                currencyService
                        .getCurrencyRates()
                        .stream()
                        .collect(Collectors.toMap(CurrencyRateDTO::getCcy, Function.identity()));

        currencyRates
                .values()
                .forEach(rate->{
                    CurrencyRate currencyRate = currencyRateDAO
                            .findByCcy(rate.getCcy())
                            .orElse(new CurrencyRate());

                    currencyRate.setCcy(rate.getCcy());
                    currencyRate.setBase_ccy(rate.getBase_ccy());
                    currencyRate.setBuy(rate.getBuy());
                    currencyRate.setSale(rate.getSale());
                    currencyRate.setDate(LocalDateTime.now());

                    currencyRateDAO.save(currencyRate);
                });
        Double saleEur = Double.parseDouble(currencyRates.get("EUR").getSale());
        Double saleUsd = Double.parseDouble(currencyRates.get("USD").getSale());
        Double buyEur = Double.parseDouble(currencyRates.get("EUR").getBuy());
        Double buyUsd = Double.parseDouble(currencyRates.get("USD").getBuy());

        carDAO.findAll().forEach(car -> {

            CalculatedCurrencyPrices calculatedCurrencyPrices = currencyService.calculateCurrencyPrices( car, saleEur, saleUsd, buyEur, buyUsd);
            calculatedCurrencyPrices.setCar(car);

             calculatedCurrencyDAO.save(calculatedCurrencyPrices);

        });


    }
}
