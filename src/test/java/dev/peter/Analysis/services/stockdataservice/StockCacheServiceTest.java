package dev.peter.Analysis.services.stockdataservice;

import dev.peter.Analysis.model.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockCacheServiceTest {

    @Mock
    StockCache stockCache;

    @Mock
    StockDatabaseService stockDatabaseService;

    @InjectMocks
    StockCacheService stockCacheService;


    @Test
    void testFindIndexWithValidData(){
        LocalDate target = LocalDate.of(2024,5,5);
        List<Stock> stocks =  List.of(
                new Stock("otp", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,3), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,5), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );
        int result = stockCacheService.findIndex(target, stocks);

        assertEquals(4, result);
    }


    @Test
    void testGetFromDatabaseWithValidData(){

        String companyName = "OTP";
        LocalDate startDate = LocalDate.of(2024,5,1);
        LocalDate endDate = LocalDate.of(2024,5,6);

        List<Stock> mockStocks =  List.of(
                new Stock("otp", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,3), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,5), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        when(stockDatabaseService.getStocksFromDataBase(anyString(), any(), any())).thenReturn(mockStocks);

        List<Stock> result = stockCacheService.getFromDatabase(companyName, startDate,endDate);

        verify(stockDatabaseService).getStocksFromDataBase(companyName, startDate, endDate);
        assertFalse(result.isEmpty());

    }

    @Test
    void testGetFromDatabaseWithEmptyReturn() {
        String companyName = "OTP";
        LocalDate startDate = LocalDate.of(2024,5,1);
        LocalDate endDate = LocalDate.of(2024,5,6);

        List<Stock> dummyStocks = null;
        when(stockDatabaseService.getStocksFromDataBase(anyString(), any(), any())).thenReturn(dummyStocks);
        List<Stock> result = stockCacheService.getFromDatabase(companyName, startDate,endDate);
        verify(stockDatabaseService).getStocksFromDataBase(companyName, startDate, endDate);

        assertTrue(result.isEmpty());

    }

    @Test
    void testBuildTogether(){

        List<Stock> frontAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );
        List<Stock> fromCache =  List.of(
                new Stock("otp", LocalDate.of(2024,5,3), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );
        List<Stock> endAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,5), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        List<Stock> result = stockCacheService.buildTogether(frontAppendable, fromCache, endAppendable);

        assertEquals(6, result.size());

    }

    @Test
    void testGetStocks(){

        String companyName = "OTP";
        LocalDate startDate = LocalDate.of(2024,5,1);
        LocalDate endDate = LocalDate.of(2024,5,6);

        List<Stock> frontAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        List<Stock> fromCache =  List.of(
                new Stock("otp", LocalDate.of(2024,5,3), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        List<Stock> endAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,5), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        when(stockCache.isStockCached(companyName)).thenReturn(true);
        when(stockCache.getCachedStock(companyName)).thenReturn(fromCache);
        when(stockDatabaseService.getStocksFromDataBase(companyName, LocalDate.of(2024,5,1), LocalDate.of(2024,5,2))).thenReturn(frontAppendable);
        when(stockDatabaseService.getStocksFromDataBase(companyName, LocalDate.of(2024,5,5), LocalDate.of(2024,5,6))).thenReturn(endAppendable);

        List<Stock> result = stockCacheService.getStocks(companyName, startDate, endDate);

        assertEquals(6, result.size());

        verify(stockCache).updateCachedStock(companyName, result);
    }

    @Test
    void testGetStocksMissingDataFromDatabase(){

        String companyName = "OTP";
        LocalDate startDate = LocalDate.of(2024,5,1);
        LocalDate endDate = LocalDate.of(2024,5,6);

        List<Stock> frontAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        List<Stock> fromCache =  List.of(
                new Stock("otp", LocalDate.of(2024,5,3), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)

        );

        List<Stock> endAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        when(stockCache.isStockCached(companyName)).thenReturn(true);
        when(stockCache.getCachedStock(companyName)).thenReturn(fromCache);
        when(stockDatabaseService.getStocksFromDataBase(companyName, LocalDate.of(2024,5,1), LocalDate.of(2024,5,2))).thenReturn(frontAppendable);
        when(stockDatabaseService.getStocksFromDataBase(companyName, LocalDate.of(2024,5,5), LocalDate.of(2024,5,6))).thenReturn(endAppendable);

        List<Stock> result = stockCacheService.getStocks(companyName, startDate, endDate);

        assertEquals(5, result.size());

        verify(stockCache).updateCachedStock(companyName, result);
    }

    @Test
    void testGetStocksWithMoreCachedData(){

        String companyName = "OTP";
        LocalDate startDate = LocalDate.of(2024,5,3);
        LocalDate endDate = LocalDate.of(2024,5,6);

        List<Stock> frontAppendable = new ArrayList<>();;
        List<Stock> fromCache =  List.of(
                new Stock("otp", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,3), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        List<Stock> endAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,5), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        when(stockCache.isStockCached(companyName)).thenReturn(true);
        when(stockCache.getCachedStock(companyName)).thenReturn(fromCache);
        when(stockDatabaseService.getStocksFromDataBase(companyName, LocalDate.of(2024,5,5), LocalDate.of(2024,5,6))).thenReturn(endAppendable);

        List<Stock> result = stockCacheService.getStocks(companyName, startDate, endDate);

        assertEquals(4, result.size());

        verify(stockCache).updateCachedStock(companyName, stockCacheService.buildTogether(frontAppendable, fromCache, endAppendable));
    }

    @Test
    void testGetStocksWithNoExactIndexFoundFromCache(){

        String companyName = "OTP";
        LocalDate startDate = LocalDate.of(2024,5,3);
        LocalDate endDate = LocalDate.of(2024,5,6);

        List<Stock> frontAppendable =  new ArrayList<>();

        List<Stock> fromCache =  List.of(
                new Stock("otp", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        List<Stock> endAppendable =  List.of(
                new Stock("otp", LocalDate.of(2024,5,5), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("otp", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );

        when(stockCache.isStockCached(companyName)).thenReturn(true);
        when(stockCache.getCachedStock(companyName)).thenReturn(fromCache);
        when(stockDatabaseService.getStocksFromDataBase(companyName, LocalDate.of(2024,5,5), LocalDate.of(2024,5,6))).thenReturn(endAppendable);

        List<Stock> result = stockCacheService.getStocks(companyName, startDate, endDate);

        assertEquals(3, result.size());

        verify(stockCache).updateCachedStock(companyName, stockCacheService.buildTogether(frontAppendable, fromCache, endAppendable));
    }

}