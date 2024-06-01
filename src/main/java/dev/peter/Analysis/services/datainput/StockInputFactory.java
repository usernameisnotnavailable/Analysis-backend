package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

@Service
public class StockInputFactory {

    private static final Logger logger = Logger.getLogger(StockInputFactory.class.getName());
    private final DataService dataService;
    private final TxtReader txtReader;
    private final CsvReader csvReader;

    public StockInputFactory(DataService dataService, TxtReader txtReader, CsvReader csvReader) {
        this.dataService = dataService;
        this.txtReader = txtReader;
        this.csvReader = csvReader;
    }

    public void inputStocks(String path) throws NoContentFoundException, FileNotFoundException {
        Map<String, List<Stock>> stocks;
        logger.info("Data saving request with path: " + path);
      if (path.endsWith(".txt")){
          stocks = txtReader.parseStocks(path);
      } else if (path.endsWith(".csv")) {
          stocks = csvReader.parseStocks(path);
      } else {
          throw new FileNotFoundException("Unsupported file type");
      }
        try {
            dataService.saveStocks(stocks);
        } catch(NoContentFoundException e) {
            logger.warning("Couldn't read stock details from: " + path);
            throw new NoContentFoundException("Couldn't read stock details.");
        }
    }
}
