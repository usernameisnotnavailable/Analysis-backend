package dev.peter.Analysis.services.stockdataservice;

import dev.peter.Analysis.model.Stock;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Component
public class StockCache {
    private static final StockCache STOCK_CACHE = new StockCache();
    private final Map<String, List<Stock>> stockCache = new ConcurrentHashMap<>();
    private final Map<String, LocalDate> lastModificationDate = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private final Logger logger = Logger.getLogger(StockCache.class.getName());

    private StockCache(){

    }

    public List<Stock> getCachedStock(String companyName){
        logger.info("Stocks were requested from cache for: " + companyName);
        if (companyName != null && stockCache.containsKey(companyName)) {
            return Collections.unmodifiableList(stockCache.get(companyName));
        }
        return Collections.emptyList();
    }
    public Map<String, List<Stock>> getAllCachedStocks(){
        return Collections.unmodifiableMap(stockCache);
    }

    public void updateCachedStock(String companyName, List<Stock> stocks){
        if (companyName != null && stocks != null) {
        synchronized (this.lock){
            stockCache.put(companyName, cacheBuilder(companyName, stocks));
            lastModificationDate.put(companyName, LocalDate.now());
            }
        }
    }

    public boolean isStockCached(String companyName){
        return stockCache.containsKey(companyName);
    }

    public Map<String, LocalDate> getAllLastModificationDate(){
        return lastModificationDate;
    }

    private List<Stock> cacheBuilder(String companyName, List<Stock> inputStocks){
        if (!stockCache.containsKey(companyName)) {
            return inputStocks;
        }
        List<Stock> fromCache = STOCK_CACHE.getCachedStock(companyName);
        List<Stock> result = new ArrayList<>();

        int i = 0, c = 0;
        while (i < inputStocks.size() && c < fromCache.size()){
            if (inputStocks.get(i).getTradeDate().isBefore(fromCache.get(c).getTradeDate())){
                result.add(inputStocks.get(i));
                i++;
            } else if (inputStocks.get(i).getTradeDate().isEqual(fromCache.get(c).getTradeDate())) {
                result.add(fromCache.get(c));
                i++;
                c++;
            } else {
                result.add(fromCache.get(c));
                c++;
            }
        }

        if (i < inputStocks.size()){
            for (int j = i; j < inputStocks.size(); j++){
                result.add(inputStocks.get(j));
            }
        } else if (c < fromCache.size()){
            for (int k = c; k < fromCache.size(); k++) {
                result.add(fromCache.get(k));
            }
        }

        return result;
    }

}
