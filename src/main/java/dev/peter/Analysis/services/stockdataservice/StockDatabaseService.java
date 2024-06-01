package dev.peter.Analysis.services.stockdataservice;

import com.mysql.cj.exceptions.UnableToConnectException;
import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class StockDatabaseService {
    private final Logger logger = Logger.getLogger(StockDatabaseService.class.getName());
    private final StockRepository stockRepository;

    public StockDatabaseService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    protected List<Stock> getStocksFromDataBase(String companyName, LocalDate from, LocalDate to){
        List<Stock> retrievedFromDatabase = stockRepository.getStocksByCompanyNameAndTradeDateBetween(companyName, from, to);
        retrievedFromDatabase.sort(Comparator.comparing(Stock::getTradeDate));
        return retrievedFromDatabase;
    }

    protected void saveAll(List<Stock> stocks){
        try {
            stockRepository.saveAll(stocks);
            logger.info(String.format("Stock were saved for: %s from: %s to: %s ",
                    stocks.get(0).getCompanyName(),
                    stocks.get(0).getTradeDate(),
                    stocks.get(stocks.size() - 1).getTradeDate()));
        } catch (UnableToConnectException e){
            logger.warning("Could not connect to database to save stock input");
            throw new UnableToConnectException("Could not connect to database to save stock input");
        }
    }
}
