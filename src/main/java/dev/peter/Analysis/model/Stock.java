package dev.peter.Analysis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private String currency;

    private Double averagePrice;
    // amount of shares * share price
    private Double marketCapitalization;

    public Stock(String companyName, LocalDate tradeDate, Double closePrice, Integer tradingVolume, Double tradingVolumeHUF, Double tradingVolumeEUR, Integer numberOfTrades, Double openPrice, Double minPrice, Double maxPrice, String currency, Double averagePrice, Double marketCapitalization) {
        this.companyName = companyName;
        this.tradeDate = tradeDate;
        this.closePrice = closePrice;
        this.tradingVolume = tradingVolume;
        this.tradingVolumeHUF = tradingVolumeHUF;
        this.tradingVolumeEUR = tradingVolumeEUR;
        this.numberOfTrades = numberOfTrades;
        this.openPrice = openPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.currency = currency;
        this.averagePrice = averagePrice;
        this.marketCapitalization = marketCapitalization;
    }
}
