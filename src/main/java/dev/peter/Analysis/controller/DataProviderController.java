package dev.peter.Analysis.controller;

import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class DataProviderController {
    Logger logger = Logger.getLogger(DataProviderController.class.getName());
    private final DataService dataService;

    public DataProviderController(DataService dataService) {
        this.dataService = dataService;

    }

    @GetMapping("/stocks")
    public ResponseEntity<?> baseStockDataRequest(
            @RequestParam(name = "stock") String stockName,
            @RequestParam(name = "from")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(name = "to")
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate to){

        if (from.isAfter(to))

            return ResponseEntity.badRequest().body("The request until date cannot be before the from date");


        List<Stock> stocks = dataService.getStocksByNameForTimePeriod(stockName, from, to);

        logger.info(String.format("Requested stock info: Stock name: %s from: %s, to: %s", stockName, from, to));

        if (stocks == null || stocks.isEmpty()){
            return ResponseEntity.ok().body(
                    Response.builder()
                            .status("Empty data")
                            .message(String.format("No data found with company name: %s, from: %s, to: %s" , stockName, from, to)));
        } else {
            return ResponseEntity.ok(stocks);
        }
    }
}
