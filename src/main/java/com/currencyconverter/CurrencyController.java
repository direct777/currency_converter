package com.currencyconverter;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin(origins = "http://localhost:8081")  // Allow requests from frontend
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeRateService exchangeRateService; // Use ExchangeRateService to fetch rates

    // Endpoint for fetching all exchange rates
    @GetMapping("/rates")
    public ResponseEntity<?> getRates() {
        try {
            return ResponseEntity.ok(exchangeRateService.getExchangeRates());
        } catch (InvalidCurrencyException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // New endpoint to convert currency
    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency(
        @RequestParam String source, 
        @RequestParam String target, 
        @RequestParam double value,
        @RequestParam(required = false) Locale locale) {

        try {
            // Use default locale if not provided
            Locale responseLocale = (locale == null) ? Locale.getDefault() : locale;
            String convertedValue = currencyService.convertCurrency(source.toUpperCase(), target.toUpperCase(), value, responseLocale);
            return ResponseEntity.ok(convertedValue);
        } catch (InvalidCurrencyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    // Endpoint to manually clear exchange rates cache
    @GetMapping("/clear-cache")
    public ResponseEntity<?> clearCache() {
        exchangeRateService.clearExchangeRatesCache();
        return ResponseEntity.ok("Exchange rates cache cleared.");
    }
}
