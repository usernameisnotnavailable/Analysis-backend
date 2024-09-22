package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class TxtReader extends FileReader implements DataReader {

    private final Logger logger = Logger.getLogger(TxtReader.class.getName());

    public TxtReader(){

    }


    public TxtReader(String path) {
        super(path);
    }


    @Override
    public Map<String, List<Stock>> readData() throws NoContentFoundException, FileNotFoundException {
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

}
