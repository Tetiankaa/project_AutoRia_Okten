package autoria.scheduler;

import autoria.dto.CurrencyRateDTO;
import autoria.entity.CalculatedCurrencyPrices;
import autoria.entity.CurrencyRate;
import autoria.entity.enums.Currency;
import autoria.entity.enums.Roles;
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

    @Scheduled(cron = "0 * * * * ?")
    public void updateCurrencyRates() throws IOException {
        Map<String, CurrencyRateDTO> currencyRates  =
                currencyService
                        .getCurrencyRates()
                        .stream()
                        .collect(Collectors.toMap(CurrencyRateDTO::getCcy, Function.identity()));

        currencyRates
                .values()
                .forEach(rate->{

                    CurrencyRate currencyRate = new CurrencyRate();

                    currencyRate.setCcy(rate.getCcy());
                    currencyRate.setBase_ccy(rate.getBase_ccy());
                    currencyRate.setBuy(rate.getBuy());
                    currencyRate.setSale(rate.getSale());
                    currencyRate.setDate(LocalDateTime.now());

                    currencyRateDAO.save(currencyRate);
                });

        carDAO.findAll().forEach(car -> {

            CalculatedCurrencyPrices calculatedCurrencyPrices = new CalculatedCurrencyPrices();
                calculatedCurrencyPrices.setDate(LocalDateTime.now());

            Double enteredPrice = car.getEnteredPrice();
            Currency currency = car.getCurrency();

            if (currency.equals(Currency.UAH)){
                Double saleEur = Double.parseDouble(currencyRates.get("EUR").getSale());
                Double saleUsd = Double.parseDouble(currencyRates.get("USD").getSale());

                calculatedCurrencyPrices.setUAH(enteredPrice);
                calculatedCurrencyPrices.setEUR(enteredPrice / saleEur);
                calculatedCurrencyPrices.setEUR(enteredPrice / saleUsd);

                calculatedCurrencyPrices.setRateUSD(saleUsd);
                calculatedCurrencyPrices.setRateEUR(saleEur);
            }
            if (currency.equals(Currency.EUR)){
                Double buyEur = Double.parseDouble(currencyRates.get("EUR").getBuy());
                Double saleUsd = Double.parseDouble(currencyRates.get("USD").getSale());

                calculatedCurrencyPrices.setEUR(enteredPrice);
                calculatedCurrencyPrices.setUAH(enteredPrice * buyEur);
                calculatedCurrencyPrices.setUSD(enteredPrice * buyEur / saleUsd);

                calculatedCurrencyPrices.setRateUSD(saleUsd);
                calculatedCurrencyPrices.setRateEUR(buyEur);
            }
            if (currency.equals(Currency.USD)){
                Double buyEur = Double.parseDouble(currencyRates.get("EUR").getBuy());
                Double buyUsd = Double.parseDouble(currencyRates.get("USD").getBuy());

                calculatedCurrencyPrices.setUSD(enteredPrice);
                calculatedCurrencyPrices.setUAH(enteredPrice * buyUsd);
                calculatedCurrencyPrices.setEUR(enteredPrice * buyUsd / buyEur);

                calculatedCurrencyPrices.setRateUSD(buyUsd);
                calculatedCurrencyPrices.setRateEUR(buyEur);
            }

        });


    }


}
