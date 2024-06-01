package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface FileReader {
    Map<String, List<Stock>> parseStocks(String path) throws NoContentFoundException, FileNotFoundException;
}
