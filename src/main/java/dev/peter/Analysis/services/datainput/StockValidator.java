package dev.peter.Analysis.services.datainput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StockValidator {

    boolean isValidData(String[] data){

        if (!isValidString(data[0])) return false;
        if (!isValidDate(data[1])) return false;
        if (!isValidDouble(data[2])) return false;
        if (!isValidInteger(data[3])) return false;
        if (!isValidDouble(data[4])) return false;
        if (!isValidDouble(data[5])) return false;
        if (!isValidInteger(data[6])) return false;
        if (!isValidDouble(data[7])) return false;
        if (!isValidDouble(data[8])) return false;
        if (!isValidDouble(data[9])) return false;
        if (!isValidString(data[10])) return false;
        if (!isValidDouble(data[11])) return false;
        if (!isValidDouble(data[12])) return false;

        return true;
    }

    boolean isValidString(String string){
        return string.length() >= 2;
    }

    boolean isValidDate(String date) {
        try {
            String pattern = "yyyy.MM.dd.";
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(date, dateFormat);
        } catch (DateTimeParseException e){
            return false;
        }
        return true;
    }

    boolean isValidInteger(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    boolean isValidDouble(String number) {
        try {
            Double.parseDouble(number);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

}
