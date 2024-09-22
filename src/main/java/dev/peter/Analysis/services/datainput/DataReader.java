package dev.peter.Analysis.services.datainput;

import dev.peter.Analysis.exceptions.NoContentFoundException;
import dev.peter.Analysis.model.Stock;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface DataReader {
    Map<String, List<Stock>> readData() throws NoContentFoundException, FileNotFoundException;
}
