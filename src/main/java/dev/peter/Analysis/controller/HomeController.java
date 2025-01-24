package dev.peter.Analysis.controller;

import dev.peter.Analysis.services.stockdataservice.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DataService dataService;

    public HomeController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/")
    public ResponseEntity<?> mainPage(){

        return ResponseEntity.status(200).build();
    }

    @GetMapping("/getStockList")
    public ResponseEntity<?> getStockList(){
        return ResponseEntity.ok(dataService.getcompanyList());
    }

}