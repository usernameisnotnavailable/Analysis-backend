package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class StockInputService {

    private final DataService dataService;

    private final Logger logger = Logger.getLogger(StockInputService.class.getName());


    public StockInputService(DataService dataService) {
        this.dataService = dataService;
    }

    public void read(String path) throws FileNotFoundException, NoContentFoundException {
        DataReader dataReader = StockInputFactory.createReader(path);
        Map<String, List<Stock>> stocks = dataReader.readData();

        try {
            dataService.saveStocks(stocks);
        } catch(NoContentFoundException e) {
            logger.warning("Couldn't read stock details from: " + path);
            throw new NoContentFoundException("Couldn't read stock details.");
        }
    }
}
