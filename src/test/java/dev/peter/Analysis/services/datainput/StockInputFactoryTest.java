package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockInputFactoryTest {

    @Mock
    TxtReader txtReader;
    @Mock
    CsvReader csvReader;
    @Mock
    DataService dataService;
    @InjectMocks
    StockInputFactory stockInputFactory;

    @Test
    public void testInputStocksWithTxt() throws NoContentFoundException, FileNotFoundException {
        String mockPath = "this/is/a/mock/path.txt";

        List<Stock> stocks = List.of(new Stock(), new Stock(), new Stock());
        Map<String, List<Stock>> mockStocks = Map.of("OTP", stocks);

        when(txtReader.parseStocks(mockPath)).thenReturn(mockStocks);

        stockInputFactory.inputStocks(mockPath);

        verify(dataService).saveStocks(mockStocks);
    }

    @Test
    public void testInputStocksWithCsv() throws NoContentFoundException, FileNotFoundException {
        String mockPath = "this/is/a/mock/path.csv";

        List<Stock> stocks = List.of(new Stock(), new Stock(), new Stock());
        Map<String, List<Stock>> mockStocks = Map.of("OTP", stocks);

        when(csvReader.parseStocks(mockPath)).thenReturn(mockStocks);

        stockInputFactory.inputStocks(mockPath);

        verify(dataService).saveStocks(mockStocks);
    }

    @Test
    public void testInputStocksWithNotSupportedExtension() throws NoContentFoundException {
        String mockPath = "this/is/a/mock/path.xlsx";

        Executable executable = () -> stockInputFactory.inputStocks(mockPath);

        assertThrows(FileNotFoundException.class, executable);
    }
}