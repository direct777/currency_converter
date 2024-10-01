package com.currencyconverter;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    @Value("${swop.api.key}")
    private String apiKey;

    @Value("${swop.api.url.rates}")
    private String apiUrl;

    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("exchangeRates") // Caching annotation
    public List<ExchangeRate> getExchangeRates() throws InvalidCurrencyException {        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "ApiKey " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ExchangeRate[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                ExchangeRate[].class
            );
            
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new InvalidCurrencyException("Error fetching exchange rates: " + e.getMessage());
        }
    }

    // Method to clear the cache
    @CacheEvict(value = "exchangeRates", allEntries = true)
    public void clearExchangeRatesCache() {        
    }

     // Schedule cache clearing using the injected fixedRate value from properties
     @Scheduled(fixedRateString = "${cache.eviction.fixedRate}")
    public void scheduledCacheClear() {
        clearExchangeRatesCache();
    }
}
