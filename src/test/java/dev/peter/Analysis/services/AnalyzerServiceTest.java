package dev.peter.Analysis.services;

import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyzerServiceTest {

    private static List<Stock> mockStocks;
    @Mock
    private DataService dataService;

    @InjectMocks
    private AnalyzerService analyzerService;

    @BeforeAll
    public static void setUp() {
        mockStocks = List.of(
                new Stock("otp", LocalDate.of(2024, 5, 1), 100.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 2), 150.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 3), 200.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 4), 250.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 5), 300.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 6), 350.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 7), 100.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 8), 150.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 9), 200.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 10), 250.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 11), 300.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 12), 350.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 13), 350.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 14), 350.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0),
                new Stock("otp", LocalDate.of(2024, 5, 15), 350.0, 100, 100.0, 100.0, 100, 100.0, 100.0, 100.0, "HUF", 100.0, 100.0)
        );
    }

    @Test
    void testGetAverage() {
        when(dataService.getStocksByNameForTimePeriod(anyString(), any(), any()))
                .thenReturn(mockStocks);

        double expected = 250;
        assertEquals(expected, analyzerService.getAverage(anyString(), any(), any()));

    }

    @Test
    void testMovingAverageWithDifferentPeriods() {
        when(dataService.getStocksByNameForTimePeriod(anyString(), any(), any()))
                .thenReturn(mockStocks);

        List<Double> expectedFor3 = List.of(150d, 200d, 250d, 300d, 250d, 200d, 150d, 200d, 250d, 300d,333.33,350d,350d);
        List<Double> expectedFor4 = List.of(200d, 250d, 262.5, 237.5, 212.5, 187.5, 200d, 250d,293.75,325d,343.75);

        assertIterableEquals(expectedFor3, analyzerService.simpleMovingAverage(anyString(), any(), any(), 3));
        assertIterableEquals(expectedFor4, analyzerService.simpleMovingAverage(anyString(), any(), any(), 4));
    }

    @Test
    void testExponentialMovingAverageWithDifferentPeriods(){
        when(dataService.getStocksByNameForTimePeriod(anyString(), any(), any()))
                .thenReturn(mockStocks);
        List<Double> expected3 = List.of(150.0, 175.0, 212.5, 256.25, 303.13, 201.57, 175.79, 187.9, 218.95, 259.48,304.74,327.37,338.69,344.35);
        List<Double> expected4 = List.of(200.0, 220.0,252.0,291.2,214.72, 188.83,193.3, 215.98, 249.59, 289.75, 313.85, 328.31, 336.99);

        assertIterableEquals(expected3, analyzerService.exponentialMovingAverage(anyString(), any(), any(), 3));
        assertIterableEquals(expected4, analyzerService.exponentialMovingAverage(anyString(), any(), any(), 4));
    }

    @Test
    void testRounding(){
        double expected = 10.55;
        double result = analyzerService.roundToTwoDecimals(10.554);
        double expected2 = 187.89;
        double result2 = analyzerService.roundToTwoDecimals(187.894999999999999999999);

        assertEquals(expected, result);
        assertEquals(expected2, result2, 0.1);
    }



}