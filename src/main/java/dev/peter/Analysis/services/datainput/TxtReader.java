package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class TxtReader implements FileReader {

    private final Logger logger = Logger.getLogger(FileReader.class.getName());
    private final StockValidator stockValidator = new StockValidator();

    @Override
    public Map<String, List<Stock>> parseStocks(String path) throws NoContentFoundException, FileNotFoundException {
        List<String> inputData;

        try {
            inputData = Files.readAllLines(Path.of(path));
            if (inputData.isEmpty()){
                throw new NoContentFoundException("The file is empty or can't be read");
            }
        } catch (IOException e) {
            logger.warning(String.format("File could not be located on: %s", path));
            throw new FileNotFoundException("File could not be located on the provided path.");
        }

        inputData = formatInput(inputData);

        Map<String, List<Stock>> inputStocks = inputData.stream()
                .map(line -> line.split("[\\W+&&[^.]]+"))
                .filter(this::isValid)
                .map(this::buildStock)
                .collect(Collectors.groupingBy(
                        Stock::getCompanyName,
                        Collectors.toList()
                ));

        for (Map.Entry<String, List<Stock>> inputByCompany: inputStocks.entrySet()) {
            inputByCompany.getValue().sort(Comparator.comparing(Stock::getTradeDate));
        }

        return inputStocks;
        
    }

    List<String> formatInput(List<String> input){
        return input.subList(1, input.size()).stream()
                .filter(trim -> !trim.isEmpty())
                .collect(Collectors.toList());
    }

    private boolean isValid(String[] data){
        return stockValidator.isValidData(data);
    }

    Stock buildStock(String[] details){
        String pattern = "yyyy.MM.dd.";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);

        return new Stock(
                details[0],
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
                Double.parseDouble(details[12])

        );
    }

}
