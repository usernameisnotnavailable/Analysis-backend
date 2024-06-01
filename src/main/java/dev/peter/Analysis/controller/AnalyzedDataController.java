package dev.peter.Analysis.controller;

import dev.peter.Analysis.controller.DTO.DataRequest;
import dev.peter.Analysis.services.AnalyzerService;
import dev.peter.Analysis.services.stockdataservice.DataService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnalyzedDataController {
    private final DataService dataService;
    private final AnalyzerService analyzerService;

    public AnalyzedDataController(
            DataService dataService,
            AnalyzerService analyzerService) {
        this.dataService = dataService;
        this.analyzerService = analyzerService;
    }

    @PostMapping("/get-data")
    public ResponseEntity<?> getSomeData(@RequestBody DataRequest dataRequest){
        Double average = analyzerService.getAverage(dataRequest.getCompanyName(), dataRequest.getStartDate(), dataRequest.getEndDate());
        return ResponseEntity.ok().body(average);
    }

    @PostMapping("/get-sma/")
    public ResponseEntity<?> getSMA(@RequestBody DataRequest dataRequest, @RequestBody int period){
        List<Double> SMA = analyzerService.simpleMovingAverage(
                dataRequest.getCompanyName(),
                dataRequest.getStartDate(),
                dataRequest.getEndDate(),
                period);

        return ResponseEntity.ok().body("All good");
    }
}
