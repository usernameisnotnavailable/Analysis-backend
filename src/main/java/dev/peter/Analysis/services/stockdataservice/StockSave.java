package dev.peter.Analysis.services.stockdataservice;

import dev.peter.Analysis.model.Stock;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Service
public class StockSave {

    private final Logger logger = Logger.getLogger(StockSave.class.getName());
    private final StockDatabaseService stockDatabaseService;

    public StockSave(StockDatabaseService stockDatabaseService) {
        this.stockDatabaseService = stockDatabaseService;
    }

    protected synchronized void saveStocks(Map<String, List<Stock>> inputStocks) {

        Map<String, List<Stock>> stocksFromDatabase = loadStocksFromDatabase(inputStocks);
        Map<String, List<Stock>> missingFromDatabase = new HashMap<>();

        if (stocksFromDatabase.isEmpty()) {
            saveToDatabase(inputStocks);
        } else {

            for (Map.Entry<String, List<Stock>> inputStocksByCompany : inputStocks.entrySet()) {
                String companyName = inputStocksByCompany.getKey();
                if (stocksFromDatabase.containsKey(companyName)) {
                    List<Stock> checkedDifference = getMissingStocksPerCompany(stocksFromDatabase.get(companyName), inputStocksByCompany.getValue(), companyName);
                    if (!checkedDifference.isEmpty()) {
                        missingFromDatabase.put(companyName, checkedDifference);
                    }
                } else {
                    missingFromDatabase.put(companyName, inputStocksByCompany.getValue());
                }
            }
            if (!missingFromDatabase.isEmpty()){
                saveToDatabase(missingFromDatabase);
            }
        }
    }

    private List<Stock> getMissingStocksPerCompany(
            List<Stock> stocksByCompanyFromDatabase,
            List<Stock> stocksByCompanyFromInput,
            String companyName) {

        int input = 0;
        int saved = 0;
        List<Stock> missingStocks = new ArrayList<>();

        while (input < stocksByCompanyFromInput.size() && saved < stocksByCompanyFromDatabase.size()) {
            if (stocksByCompanyFromInput.get(input).getTradeDate().isEqual(stocksByCompanyFromDatabase.get(saved).getTradeDate())) {
                input += 1;
                saved += 1;
            } else if (stocksByCompanyFromInput.get(input).getTradeDate().isBefore(stocksByCompanyFromDatabase.get(saved).getTradeDate())) {
                missingStocks.add(stocksByCompanyFromInput.get(input));
                input += 1;
            } else {
                saved += 1;
            }
        }

        for (int i = input; i < stocksByCompanyFromInput.size(); i++) {
            missingStocks.add(stocksByCompanyFromInput.get(i));
        }

        return missingStocks;
    }

    private void saveToDatabase(Map<String, List<Stock>> inputStocks) {
        for (Map.Entry<String, List<Stock>> companyStocks : inputStocks.entrySet()) {
            stockDatabaseService.saveAll(companyStocks.getValue());
        }
    }

    private Map<String, List<Stock>> loadStocksFromDatabase(Map<String, List<Stock>> requestedStocks) {
        Map<String, List<Stock>> retrieved = new HashMap<>();

        for (Map.Entry<String, List<Stock>> companyStocks : requestedStocks.entrySet()) {
            String companyName = companyStocks.getKey();
            LocalDate starDate = companyStocks.getValue().get(0).getTradeDate();
            LocalDate endDate = companyStocks.getValue().get(companyStocks.getValue().size() - 1).getTradeDate();

            List<Stock> fromDatabase = stockDatabaseService.getStocksFromDataBase(companyName, starDate, endDate);
            if (!fromDatabase.isEmpty()) {
                retrieved.put(companyName, fromDatabase);
            }

        }
        return retrieved;
    }

}
