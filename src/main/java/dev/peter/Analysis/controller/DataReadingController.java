package dev.peter.Analysis.controller;

import dev.peter.Analysis.controller.DTO.Response;
import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.services.datainput.StockInputFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class DataReadingController {
    private final StockInputFactory stockInputFactory;

    private final Logger logger = Logger.getLogger(DataReadingController.class.getName());

    public DataReadingController(StockInputFactory stockInputFactory) {
        this.stockInputFactory = stockInputFactory;
    }

    @PostMapping("/read-data")
    public ResponseEntity<?> readData(@RequestBody List<String> paths) {
        String customResponse = "";
        logger.info("Post request on /read-data");
        if (paths.isEmpty()) {
            customResponse = "No path provided in the request for /read-data";
            logger.info(customResponse);
            return ResponseEntity.status(400).body(Response.builder().status("error").message(customResponse).build());
        } else {
            for (String path : paths) {
                try {
                    stockInputFactory.inputStocks(path);
                    customResponse += "File read from: " + path;
                } catch (NoContentFoundException | FileNotFoundException e) {
                    customResponse += "Error on path: " + path + " Error message: " + e;
                }
            }
        }

        return ResponseEntity.status(200).body(Response.builder().status("Request successfully processed").message(customResponse).build());
    }
}
