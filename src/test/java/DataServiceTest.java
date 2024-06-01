/*
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

import dev.peter.Analysis.AnalysisApplication;
import dev.peter.Analysis.model.Stock;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import dev.peter.Analysis.repository.StockRepository;
import dev.peter.Analysis.services.databaseservice.DataService;

@SpringBootTest(classes = AnalysisApplication.class)
public class DataServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private DataService dataService;

    @Test
    public void testSMA() {
        List<Stock> stocks = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2023, 5, 15);
        LocalDate endDate = LocalDate.of(2023, 5, 19);
        Stock stock = new Stock();
        stock.setCompanyName("OTP");
        stock.setTradeDate(LocalDate.of(2023, 5, 15));
        stock.setClosePrice(40.0);
        stocks.add(stock);
        stock.setTradeDate(LocalDate.of(2023, 5, 16));
        stock.setClosePrice(50.0);
        stocks.add(stock);
        stock.setTradeDate(LocalDate.of(2023, 5, 17));
        stock.setClosePrice(60.0);
        stocks.add(stock);
        stock.setTradeDate(LocalDate.of(2023, 5, 18));
        stock.setClosePrice(40.0);
        stocks.add(stock);
        stock.setTradeDate(LocalDate.of(2023, 5, 19));
        stock.setClosePrice(50.0);
        stocks.add(stock);
        when(stockRepository.getStocksByCompanyNameAndTradeDateBetween("OTP", startDate, endDate)).thenReturn(stocks);
        List<Double> result = dataService.simpleMovingAverage("OTP", startDate, endDate, 3);
        List<Double> expected = List.of(50.0, 50.0, 50.0);

        assertIterableEquals(expected, result);

        verify(stockRepository, times(1)).getStocksByCompanyNameAndTradeDateBetween(anyString(), any(), any());


    }
}*/
