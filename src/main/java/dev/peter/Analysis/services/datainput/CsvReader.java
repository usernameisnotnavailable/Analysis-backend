package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.model.Stock;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class CsvReader implements FileReader {

    private final Logger logger = Logger.getLogger(CsvReader.class.getName());

    private final StockValidator stockValidator = new StockValidator();

    @Override
    public Map<String, List<Stock>> parseStocks(String path) {
        List<String> inputData = new ArrayList<>();

        try {
            inputData = Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            logger.warning(String.format("File could not be read from: %s", path));
        }

        if (inputData.isEmpty()){
            logger.warning("Input file is empty");
        }

/*       inputData = inputData.subList(1, inputData.size()).stream()
                .filter(trim -> !trim.isEmpty())
                .collect(Collectors.toList());*/


        Map<String, List<Stock>> parsedStocks = inputData.stream()
                .skip(1)
                .map(line -> line.split(","))
                .filter(this::isValid)
                .map(this::buildStock)
                .collect(Collectors.groupingBy(
                        Stock::getCompanyName,
                        Collectors.toList()
                ));

        if (inputData.size() - 1 - parsedStocks.size() == 0){
            logger.info(String.format("From: %d lines, %d stocks has been created, missing or incorrect data on %d lines.", inputData.size(), parsedStocks.size(), 0));
        } else {
            logger.warning(String.format("From: %d lines, %d stocks has been created, missing or incorrect data on %d lines.", inputData.size(), parsedStocks.size(), inputData.size() - parsedStocks.size()));
        }


        return parsedStocks;
    }

    private boolean isValid(String[] data){
        return stockValidator.isValidData(data);
    }

    private Stock buildStock(String[] details){
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
