package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TxtReaderTest {

    @InjectMocks
    private TxtReader txtReader;

    private MockedStatic<Files> mockedFiles;

    @BeforeEach
    void setUp(){
        mockedFiles = Mockito.mockStatic(Files.class);
    }

    @AfterEach
    void tearDown() {
        mockedFiles.close();
    }

    @Test
    void testParseStockWithValidData() throws NoContentFoundException, FileNotFoundException {
        String stringPath = "path/to/mock.txt";
        List<String> mockData = Arrays.asList(
                "this is an extra line with details",
                "OTP    2024.05.02.      100     1000    1000    1000.123    10  80  70  110     HUF     75.15  155555",
                "OTP    2024.05.01.      100     1000    1000    1000.123    10  80  70  110     HUF     75.15  155555",
                "AKKO    2024.05.02.     100     1000    1000    1000.123    10  80  70  110     HUF     75.15  155555"
        );

        mockedFiles.when(() -> Files.readAllLines(any(Path.class))).thenReturn(mockData);

        Map<String, List<Stock>> result = txtReader.parseStocks(stringPath);

        assertNotNull(result);
        assertEquals(2,result.size());
        assertTrue(result.containsKey("OTP"));
        assertTrue(result.containsKey("AKKO"));
        assertEquals(2, result.get("OTP").size());

    }

    @Test
    void testParseStockWithInvalidData() throws NoContentFoundException, FileNotFoundException {
        TxtReader txtReader = new TxtReader();
        String stringPath = "path/to/mock.txt";
        List<String> mockData = Arrays.asList(
                "this is an extra line with details",
                "OTP    2024.05.01.      100     1000    apple    1000.123    10  80  70  110     HUF     75.15  155555",
                "OTP    2024.05.02.      100     1000    1000    1000.123    10  80  70  110     HUF     75.15  155555",
                "AKKO    2024.05.02.     100     1000    1000    1000.123    10  80  70  110     HUF     75.15  155555"
        );

        mockedFiles.when(() -> Files.readAllLines(any(Path.class))).thenReturn(mockData);

        Map<String, List<Stock>> result = txtReader.parseStocks(stringPath);

        assertNotNull(result);
        assertEquals(1, result.get("OTP").size());
        assertEquals(1, result.get("AKKO").size());
    }

    @Test
    void testParseStockWithMissingData() throws NoContentFoundException, FileNotFoundException {
        TxtReader txtReader = new TxtReader();
        String stringPath = "path/to/mock.txt";
        List<String> mockData = Arrays.asList(
                "this is an extra line with details",
                "OTP    2024.05.01.      100     1000        1000.123    10  80  70  110     HUF     75.15  155555",
                "OTP    2024.05.02.      100     1000    1000    1000.123    10  80  70  110     HUF     75.15  155555",
                "AKKO    2024.05.02.     100     1000    1000    1000.123    10  80  70  110     HUF     75.15  155555"
        );

        mockedFiles.when(() -> Files.readAllLines(any(Path.class))).thenReturn(mockData);

        Map<String, List<Stock>> result = txtReader.parseStocks(stringPath);

        assertNotNull(result);
        assertEquals(1, result.get("OTP").size());
        assertEquals(1, result.get("AKKO").size());
    }

    @Test
    void testParseStockWithNoData() throws NoContentFoundException, FileNotFoundException {
        TxtReader txtReader = new TxtReader();
        String stringPath = "path/to/mock.txt";
        List<String> mockData = List.of();

        mockedFiles.when(() -> Files.readAllLines(any(Path.class))).thenReturn(mockData);

        Executable executable = () -> txtReader.parseStocks(stringPath);

       assertThrows(NoContentFoundException.class, executable);
    }


    @Test
    public void testBuildStockWithValidData(){

        String[] validData = {"OTP", "2024.05.01.", "100", "1000", "1000", "1000.123", "10", "80", "70",  "110", "HUF", "75.15", "155555"};

        Stock resultStock = txtReader.buildStock(validData);

        assertNotNull(resultStock);

    }

    @Test
    public void testBuildStockWithInvalidData(){
        String[] invalidData = {"OTP", "2024.05.01.", "invalid_number", "1000", "1000", "1000.123", "10", "80", "70",  "110", "HUF", "75.15", "155555"};

        Executable executable = () -> txtReader.buildStock(invalidData);

        assertThrows(NumberFormatException.class, executable);

    }

}