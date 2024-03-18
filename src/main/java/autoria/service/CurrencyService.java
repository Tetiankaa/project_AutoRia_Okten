package autoria.service;

import autoria.dto.CurrencyRateDTO;
import autoria.entity.CalculatedCurrencyPrices;
import autoria.entity.Car;
import autoria.entity.CurrencyRate;
import autoria.entity.enums.Currency;
import autoria.repository.CalculatedCurrencyPricesDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    @Value("${privatbank.api}")
    private  String privatbankApi;

    private final ObjectMapper objectMapper;
    private final CalculatedCurrencyPricesDAO calculatedCurrencyDAO;

    public List<CurrencyRateDTO> getCurrencyRates() throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(privatbankApi)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if (!response.isSuccessful()){
            throw new IOException("Failed to get currency rates");
        }
        
        byte[] bytes = response.body().bytes();
        String payload = new String(bytes);

        return objectMapper.readValue(payload, new TypeReference<>() {});

    }

    public CalculatedCurrencyPrices calculateCurrencyPrices( Car car, Double saleEur, Double saleUsd, Double buyEur,Double buyUsd ){

        CalculatedCurrencyPrices calculatedCurrencyPrices = calculatedCurrencyDAO
                .findByCar(car)
                .orElse(new CalculatedCurrencyPrices());

        calculatedCurrencyPrices.setDate(LocalDateTime.now());

        Double enteredPrice = car.getEnteredPrice();
        Currency currency = car.getCurrency();

        switch (currency){
            case UAH:
                calculatedCurrencyPrices.setUAH(enteredPrice);
                calculatedCurrencyPrices.setEUR(enteredPrice / saleEur);
                calculatedCurrencyPrices.setUSD(enteredPrice / saleUsd);

                calculatedCurrencyPrices.setRateUSD(saleUsd);
                calculatedCurrencyPrices.setRateEUR(saleEur);
                break;
            case EUR:
                calculatedCurrencyPrices.setEUR(enteredPrice);
                calculatedCurrencyPrices.setUAH(enteredPrice * buyEur);
                calculatedCurrencyPrices.setUSD(enteredPrice * buyEur / saleUsd);

                calculatedCurrencyPrices.setRateUSD(saleUsd);
                calculatedCurrencyPrices.setRateEUR(buyEur);
                break;
            case USD:
                calculatedCurrencyPrices.setUSD(enteredPrice);
                calculatedCurrencyPrices.setUAH(enteredPrice * buyUsd);
                calculatedCurrencyPrices.setEUR(enteredPrice * buyUsd / saleEur);

                calculatedCurrencyPrices.setRateUSD(buyUsd);
                calculatedCurrencyPrices.setRateEUR(saleEur);
                break;
            default:
                throw new IllegalArgumentException("Unsupported currency: " + currency);

        }

        return calculatedCurrencyPrices;
    }
}
