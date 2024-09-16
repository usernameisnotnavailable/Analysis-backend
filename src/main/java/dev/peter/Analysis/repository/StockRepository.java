package dev.peter.Analysis.repository;

import dev.peter.Analysis.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    LinkedList<Stock> getStocksByCompanyNameAndTradeDateBetween(String companyName, LocalDate startDate, LocalDate endDate);



}
