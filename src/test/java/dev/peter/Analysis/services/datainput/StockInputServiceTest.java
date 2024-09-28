package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockInputServiceTest {

    @Mock
    DataReader dataReader;

    @Mock
    StockInputFactory stockInputFactory;
    @Mock
    DataService dataService;
    @InjectMocks
    StockInputService stockInputService;

    @Test
    public void testInputStocksWithTxt() throws NoContentFoundException, FileNotFoundException {
        String mockPath = "this/is/a/mocked/path.txt";

        List<Stock> stocks = List.of(new Stock(), new Stock(), new Stock());
        Map<String, List<Stock>> mockStocks = Map.of("OTP", stocks);

        when(stockInputFactory.createReader(mockPath)).thenReturn(dataReader);
        when(dataReader.readData()).thenReturn(mockStocks);

        stockInputService.read(mockPath);

        verify(dataService).saveStocks(mockStocks);
    }

    @Test
    public void testInputStocksWithCsv() throws NoContentFoundException, FileNotFoundException {
        String mockPath = "this/is/a/mocked/path.csv";

        List<Stock> stocks = List.of(new Stock(), new Stock(), new Stock());
        Map<String, List<Stock>> mockStocks = Map.of("OTP", stocks);
        when(stockInputFactory.createReader(mockPath)).thenReturn(dataReader);
        when(dataReader.readData()).thenReturn(mockStocks);

        stockInputService.read(mockPath);

        verify(dataService).saveStocks(mockStocks);
    }


}