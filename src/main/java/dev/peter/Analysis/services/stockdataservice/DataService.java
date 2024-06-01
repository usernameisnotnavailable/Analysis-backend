package dev.peter.Analysis.services.stockdataservice;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class DataService {
    private final Logger logger = Logger.getLogger(DataService.class.getName());
    private final StockCacheService databaseCache;
    private final StockSave stockSave;

    public DataService(StockCacheService databaseCache, StockSave stockSave) {
        this.databaseCache = databaseCache;
        this.stockSave = stockSave;
    }

    public List<Stock> getStocksByNameForTimePeriod(String companyName, LocalDate startDate, LocalDate endDate) {
        return databaseCache.getStocks(companyName, startDate, endDate);
    }

    public void saveStocks(Map<String, List<Stock>> input){
        try {
            stockSave.saveStocks(input);
        } catch(NoContentFoundException e) {
            logger.warning("Unsuccessful saving request");
            throw new NoContentFoundException("Couldn't read stock details.");
        }
    }

}
