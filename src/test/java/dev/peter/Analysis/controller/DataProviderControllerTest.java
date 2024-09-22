package dev.peter.Analysis.controller;

import dev.peter.Analysis.model.Stock;
import dev.peter.Analysis.services.stockdataservice.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DataProviderController.class)
class DataProviderControllerTest {

    @MockBean
    DataService dataService;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void testStocksEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/stocks"))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void testStocksEndpointWithParamsEmptyReturn() throws Exception {
        when(dataService.getStocksByNameForTimePeriod(anyString(), any(), any()))
                .thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/stocks?stock=OTP&from=2020-01-01&to=2021-01-01"))
                .andExpect(status().isOk())
                .andReturn();

    }


    @Test
    public void testStocksEndpointWithParamsStockReturn() throws Exception {
        List<Stock> mockStocks =  List.of(
                new Stock("OTP", LocalDate.of(2024,5,1), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("OTP", LocalDate.of(2024,5,2), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("OTP", LocalDate.of(2024,5,3), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("OTP", LocalDate.of(2024,5,4), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("OTP", LocalDate.of(2024,5,5), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0),
                new Stock("OTP", LocalDate.of(2024,5,6), 100.0,100,100.0,100.0,100,100.0,100.0,100.0,"HUF",100.0,100.0)
        );
        when(dataService.getStocksByNameForTimePeriod(anyString(), any(), any()))
                .thenReturn(mockStocks);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .param("stock", "OTP")
                .param("from", "2024-05-01")
                .param("to", "2024-05-06"))
                .andReturn();


        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testStocksEndpointWithInvalidDate() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/stocks?stock=OTP&from=2023-01-01&to=2021-01-01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("stock", "OTP")
                        .param("from", "2022-01-01")
                        .param("to", "2021-01-01"))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }


}
