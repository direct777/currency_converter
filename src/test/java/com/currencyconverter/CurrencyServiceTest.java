package com.currencyconverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyServiceTest {

    private CurrencyService currencyService;

    // Simple implementation of ExchangeRateService for testing
    private class TestExchangeRateService extends ExchangeRateService {
        public TestExchangeRateService() {
            super(null); // Passing null as RestTemplate is not used in this test implementation
        }

        @Override
        public List<ExchangeRate> getExchangeRates() {
            ExchangeRate rate1 = new ExchangeRate();
            rate1.setBaseCurrency("USD");
            rate1.setQuoteCurrency("EUR");
            rate1.setQuote(0.85);

            ExchangeRate rate2 = new ExchangeRate();
            rate2.setBaseCurrency("EUR");
            rate2.setQuoteCurrency("USD");
            rate2.setQuote(1.18);

            return Arrays.asList(rate1, rate2);
        }
    }

    @BeforeEach
    public void setUp() {
        ExchangeRateService exchangeRateService = new TestExchangeRateService();
        currencyService = new CurrencyService(exchangeRateService);
    }

    @Test
    public void testConvertCurrency() throws InvalidCurrencyException {
        double amount = 100.0;
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        Locale locale = Locale.US;

        String result = currencyService.convertCurrency(fromCurrency, toCurrency, amount, locale);
        assertEquals("€85.00", result);
    }
}