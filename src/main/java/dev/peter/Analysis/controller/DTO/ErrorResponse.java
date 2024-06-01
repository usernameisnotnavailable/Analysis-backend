package dev.peter.Analysis.controller.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponse {
    private String status;
    private String message;

}
