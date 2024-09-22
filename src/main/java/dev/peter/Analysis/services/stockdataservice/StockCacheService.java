package dev.peter.Analysis.services.stockdataservice;

import dev.peter.Analysis.model.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class StockCacheService {
    private final Logger logger = Logger.getLogger(StockCacheService.class.getName());
    private final StockCache stockCache;
    private final StockDatabaseService stockDatabaseService;

    public StockCacheService(StockDatabaseService stockDatabaseService, StockCache stockCache) {
        this.stockDatabaseService = stockDatabaseService;
        this.stockCache = stockCache;
    }

    List<Stock> getStocks(String companyName, LocalDate requestFromDate, LocalDate requestUntilDate) {
        logger.info(String.format("Stocks for %s, requested from: %s, to: %s", companyName, requestFromDate, requestUntilDate));
        List<Stock> requestedStocks;

        if (stockCache.isStockCached(companyName)) {
            List<Stock> fromCache = new ArrayList<>(stockCache.getCachedStock(companyName));
            List<Stock> frontAppendable = new ArrayList<>();
            List<Stock> endAppendable = new ArrayList<>();
            int startIndex;
            int endIndex;
            boolean isDataPulled = false;

            Stock firstFromCached = fromCache.get(0);
            Stock lastFromCached = fromCache.get(fromCache.size() - 1);

            if (requestFromDate.isBefore(firstFromCached.getTradeDate())) {
                frontAppendable = getFromDatabase(companyName, requestFromDate, firstFromCached.getTradeDate().minusDays(1L));
                isDataPulled = true;
                startIndex = 0;
            } else {
                startIndex = findIndex(requestFromDate, fromCache);
            }

            if (requestUntilDate.isAfter(lastFromCached.getTradeDate())) {
                LocalDate requestFromDateForPulling = lastFromCached.getTradeDate().plusDays(1L).isBefore(requestFromDate) ? requestFromDate : lastFromCached.getTradeDate().plusDays(1L);
                endAppendable = getFromDatabase(companyName, requestFromDateForPulling, requestUntilDate);
                isDataPulled = true;
                endIndex = fromCache.size() - 1;
            } else {
                endIndex = findIndex(requestUntilDate, fromCache);
            }

            requestedStocks = buildTogether(frontAppendable, fromCache.subList(startIndex, endIndex + 1), endAppendable);

            if (isDataPulled) {
                updateCache(companyName, buildTogether(frontAppendable, fromCache, endAppendable));
            }

        } else {
            requestedStocks = getFromDatabase(companyName, requestFromDate, requestUntilDate);
            updateCache(companyName, requestedStocks);
        }

        return requestedStocks;
    }

    List<Stock> buildTogether(List<Stock> frontAppendable, List<Stock> fromCache, List<Stock> endAppendable) {
        return Stream.of(frontAppendable, fromCache, endAppendable)
                .flatMap(List::stream)
                .toList();
    }

    int findIndex(LocalDate target, List<Stock> list) {
        int l = 0;
        int r = list.size() - 1;

        while (l <= r) {
            int m = l + (r - l) / 2;
            if (target.isEqual(list.get(m).getTradeDate())) return m;

            if (list.get(m).getTradeDate().isAfter(target)) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }

        return l;
    }

    List<Stock> getFromDatabase(String companyName, LocalDate from, LocalDate to) {
        List<Stock> resultsFromDatabase = stockDatabaseService.getStocksFromDataBase(companyName, from, to);
        return resultsFromDatabase == null ? new ArrayList<>() : resultsFromDatabase;
    }

    private void updateCache(String companyName, List<Stock> stocks) {
        stockCache.updateCachedStock(companyName, stocks);
    }

}