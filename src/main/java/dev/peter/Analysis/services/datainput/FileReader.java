package dev.peter.Analysis.services.datainput;


import dev.peter.Analysis.model.Stock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public abstract class FileReader {
    String path;

    public FileReader(String path){
        this.path = path;
    }

    private final StockValidator stockValidator = new StockValidator();

    boolean isValid(String[] data){
        return stockValidator.isValidData(data);
    }

    Stock buildStock(String[] details){
        String pattern = "yyyy.MM.dd.";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);

        return new Stock(details[0],
                LocalDate.parse(details[1], dateFormat),
                Double.parseDouble(details[2]),
                Integer.parseInt(details[3]),
                Double.parseDouble(details[4]),
                Double.parseDouble(details[5]),
                Integer.parseInt(details[6]),
                Double.parseDouble(details[7]),
                Double.parseDouble(details[8]),
                Double.parseDouble(details[9]),
                details[10],
                Double.parseDouble(details[11]),
                Double.parseDouble(details[12]));
    }
}
