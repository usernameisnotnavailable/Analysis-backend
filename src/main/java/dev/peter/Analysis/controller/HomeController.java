package dev.peter.Analysis.controller;

import dev.peter.Analysis.services.stockdataservice.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class HomeController {
    Logger logger = Logger.getLogger(HomeController.class.getName());
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
        logger.info("hit endpoint: /getStockList");
        return ResponseEntity.ok(dataService.getcompanyList());
    }

}