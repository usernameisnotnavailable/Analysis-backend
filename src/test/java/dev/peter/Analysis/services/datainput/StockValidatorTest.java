package dev.peter.Analysis.services.datainput;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StockValidatorTest {


    @InjectMocks
    StockValidator stockValidator;


    @Test
    public void testIsValidDataWithValidData(){

        String[] data = {"OTP", "2024.05.01.", "100", "1000", "1000", "1000.123", "10", "80", "70",  "110", "HUF", "75.15", "155555"};

        boolean result = stockValidator.isValidData(data);

        assertTrue(result);
    }

    @Test
    public void testIsValidDataWithInvalidData(){

        String[] data = {"OTP", "2024.05.01.", "alma", "1000", "1000", "1000.123", "10", "80", "70",  "110", "HUF", "75.15", "155555"};

        boolean result = stockValidator.isValidData(data);

        assertFalse(result);
    }

    @Test
    public void testIsValidDataWithMissingData(){

        String[] data = {"OTP", "2024.05.01.", "1000", "1000", "1000.123", "10", "80", "70",  "110", "HUF", "75.15", "155555"};

        boolean result = stockValidator.isValidData(data);

        assertFalse(result);
    }

    @Test
    public void testIsInvalidDateWithValidData(){

        String validDate = "2024.05.01.";

        boolean validDateResult = stockValidator.isValidDate(validDate);

        assertTrue(validDateResult);
    }

    @Test
    public void testIsInvalidDateWithInvalidData(){

        String invalidDate = "2024-01-01";

        boolean invalidDateResult = stockValidator.isValidDate(invalidDate);

        assertFalse(invalidDateResult);
    }

    @Test
    public void testIsValidIntegerWIthValidData(){
        String validInteger = "123";

        boolean validIntegerResult = stockValidator.isValidInteger(validInteger);

        assertTrue(validIntegerResult);
    }

    @Test
    public void testIsValidIntegerWIthInvalidData(){
        String invalidInteger = "123.123";

        boolean invalidIntegerResult = stockValidator.isValidInteger(invalidInteger);

        assertFalse(invalidIntegerResult);
    }

    @Test
    public void testIsValidDoubleWithValidData(){
        String validDouble = "123.123";

        boolean validDoubleResult = stockValidator.isValidDouble(validDouble);

        assertTrue(validDoubleResult);
    }

    @Test
    public void testIsValidDoubleWithInvalidData(){
        String invalidDouble = "";

        boolean invalidDoubleResult = stockValidator.isValidDouble(invalidDouble);

        assertFalse(invalidDoubleResult);
    }


}