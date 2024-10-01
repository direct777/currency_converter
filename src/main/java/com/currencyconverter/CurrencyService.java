package com.currencyconverter;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.text.NumberFormat;

@Service
public class CurrencyService {

    private final ExchangeRateService exchangeRateService;

    
    public CurrencyService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    // Convert currency using the fetched exchange rates
    public String convertCurrency(String source, String target, double value, Locale locale) throws InvalidCurrencyException {
        List<ExchangeRate> rates = exchangeRateService.getExchangeRates();                
        
        // Find exchange rate for the currency pair
        Optional<ExchangeRate> sourceToTargetRate = rates.stream()        
        .filter(rate -> rate.getBaseCurrency().equalsIgnoreCase(source)
                     && rate.getQuoteCurrency().equalsIgnoreCase(target))
        .findFirst();
        double result;
        if (sourceToTargetRate.isPresent()) {            
            double rate = sourceToTargetRate.get().getQuote();
            result = value * rate;
        } else {            
            Optional<ExchangeRate> targetToSourceRate = rates.stream()
                .filter(rate -> rate.getBaseCurrency().equalsIgnoreCase(target)
                            && rate.getQuoteCurrency().equalsIgnoreCase(source))
                .findFirst();

            if (targetToSourceRate.isPresent()) {
                double rate = targetToSourceRate.get().getQuote();                
                result =  value / rate;  // Reverse the rate
            } else {                
                throw new InvalidCurrencyException("Currency pair not supported or not found.");
            }
        }
        // Extract the target currency's ISO code (e.g., EUR, USD) from the target string
        String targetCurrency = target.toUpperCase();
        // Format the result based on the provided locale
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(java.util.Currency.getInstance(targetCurrency));  // Set the target currency
        String formattedResult = currencyFormatter.format(result);        

        return formattedResult; // return formatted result
    }
}
