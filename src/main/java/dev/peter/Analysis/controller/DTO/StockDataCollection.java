package dev.peter.Analysis.controller.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StockDataCollection {

    private String companyName;
    private LocalDate tradeDate;
    private Double closePrice;
    private Integer tradingVolume;
    private Double tradingVolumeHUF;
    private Double tradingVolumeEUR;
    private Integer numberOfTrades;
    private Double openPrice;
    private Double minPrice;
    private Double maxPrice;

}