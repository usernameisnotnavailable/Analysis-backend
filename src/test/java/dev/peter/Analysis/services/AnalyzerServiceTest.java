package dev.peter.Analysis.services;

import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        mockStocks =  List.of(
                new Stock("otp", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,2), 150.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,3), 200.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 250.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,5), 300.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,6), 350.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );
    }
    @Test
    void testGetAverage() {
        when(dataService.getStocksByNameForTimePeriod(anyString(), any(), any()))
                .thenReturn(mockStocks);

        double expected = 225;
        assertEquals(expected, analyzerService.getAverage(anyString(), any(), any()));

    }
}