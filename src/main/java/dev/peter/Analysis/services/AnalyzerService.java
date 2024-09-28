package dev.peter.Analysis.services;

import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyzerService {

    private final DataService dataService;

    public AnalyzerService(DataService dataService){
        this.dataService = dataService;
    }

    public Double getAverage(String companyName, LocalDate startDate, LocalDate endDate) {
        List<Stock> requestedData = dataService.getStocksByNameForTimePeriod(companyName, startDate, endDate);
        return requestedData.stream().collect(Collectors.averagingDouble(Stock::getClosePrice));
    }
    public List<Double> simpleMovingAverage(String companyName, LocalDate startDate, LocalDate endDate, int period) {

        List<Stock> stockList = dataService.getStocksByNameForTimePeriod(companyName, startDate, endDate);

        return simpleMovingAverage(stockList, period);
    }

    public List<Double> exponentialMovingAverage(String companyName, LocalDate startDate, LocalDate endDate, int period) {
        List<Stock> stockList = dataService.getStocksByNameForTimePeriod(companyName, startDate, endDate);

        return exponentialMovingAverage(stockList, period);
    }

    private List<Double> simpleMovingAverage(List<Stock> listOfStock, int period){
        List<Double> result = new ArrayList<>();
        double tempSum = 0;

        boolean isEvenPeriod = period % 2 == 0;

        if (isEvenPeriod){
            for (int i = 0; i <= period; i++) {
                if (i == 0 || i == period){
                    tempSum += listOfStock.get(i).getClosePrice() / 2;
                } else {
                    tempSum += listOfStock.get(i).getClosePrice();
                }
            }

            result.add(tempSum / period);

            for (int i = period; i < listOfStock.size() - 1; i++) {
                tempSum -= listOfStock.get(i - period).getClosePrice() / 2;
                tempSum -= listOfStock.get(i - period + 1).getClosePrice() / 2;
                tempSum += listOfStock.get(i).getClosePrice() / 2;
                tempSum += listOfStock.get(i + 1).getClosePrice() / 2;

                result.add(tempSum/period);
            }

        } else {
            for (int i = 0; i < period; i++) {
                tempSum += listOfStock.get(i).getClosePrice();
            }
            result.add(tempSum/period);

            for (int i = period; i < listOfStock.size(); i++) {
                tempSum += listOfStock.get(i).getClosePrice();
                tempSum -= listOfStock.get(i-period).getClosePrice();
                result.add(tempSum/period);
            }
        }

        return result;
    }

    private List<Double> exponentialMovingAverage(List<Stock> listOfStock, int period){
        double k = 2d / (period + 1);

        List<Double> EMA = new ArrayList<>();

        //get starting sma
        List<Double> SMA = simpleMovingAverage(listOfStock.subList(0, period), period);

        EMA.add(SMA.get(SMA.size() - 1));

        for (int i = period - 1; i < listOfStock.size(); i++) {
            Double EMAToday = (listOfStock.get(i).getClosePrice() * k) + (EMA.get(EMA.size() - 1)) * (1 - k);
            EMA.add(EMAToday);
        }

        return EMA;
    }

    private List<Double> rsi(List<Stock> stockList, int period){
        List<Double> rsiValues = new ArrayList<>();

        return rsiValues;

    }

}
