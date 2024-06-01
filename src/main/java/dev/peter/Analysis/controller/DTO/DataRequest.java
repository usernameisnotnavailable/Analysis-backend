package dev.peter.Analysis.controller.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DataRequest {
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
}
