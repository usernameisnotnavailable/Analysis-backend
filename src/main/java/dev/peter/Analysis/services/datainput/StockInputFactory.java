package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

public class StockInputFactory {

    private static final Logger logger = Logger.getLogger(StockInputFactory.class.getName());

    public static DataReader createReader(String path) throws UnsupportedOperationException{
        if (path.endsWith(".txt")){
            return new TxtReader(path);
        } else if (path.endsWith(".csv")) {
            return new CsvReader(path);
        } else {
            throw new UnsupportedOperationException("Unsupported file type");
        }
    }

    public void inputStocks(String path) throws NoContentFoundException, FileNotFoundException {
        Map<String, List<Stock>> stocks;
        logger.info("Data saving request with path: " + path);


    }
}
