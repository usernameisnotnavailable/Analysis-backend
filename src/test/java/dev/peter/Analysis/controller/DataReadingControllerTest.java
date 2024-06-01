package dev.peter.Analysis.controller;

import dev.peter.Analysis.services.datainput.StockInputFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.skyscreamer.jsonassert.JSONAssert;

import java.io.FileNotFoundException;


@WebMvcTest(DataReadingController.class)
@ExtendWith(MockitoExtension.class)
class DataReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockInputFactory stockInputFactory;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new DataReadingController(stockInputFactory)).build();
    }

    @Test
    void testReadDataNoWithoutPath() throws Exception {
        String requestBody = "[]";

        MvcResult result = mockMvc.perform(post("/read-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

                String expectedResponse = "{\"status\":\"error\",\"message\":\"No path provided in the request for /read-data\"}";
                JSONAssert.assertEquals(expectedResponse, result.getResponse().getContentAsString(), false);

        verify(stockInputFactory, never()).inputStocks(anyString());
    }

    @Test
    void testReadDataWithValidPath() throws Exception {
        String requestBody = "[\"/mock/test/path.txt\"]";

        MvcResult result = mockMvc.perform(post("/read-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponse = "{\"status\":\"Request successfully processed\", \"message\":\"File read from: /mock/test/path.txt\"}";
        String json = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedResponse, result.getResponse().getContentAsString(), false);

        verify(stockInputFactory).inputStocks("/mock/test/path.txt");
    }

    @Test
    void testReadDataWithFileNotFoundException() throws Exception {
        String requestBody = "[\"/mock/test/path.txt\"]";

        doThrow(new FileNotFoundException("File not found")).when(stockInputFactory).inputStocks("/mock/test/path.txt");


        MvcResult result = mockMvc.perform(post("/read-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponse = "{\"status\":\"Request successfully processed\",\"message\":\"Error on path: /mock/test/path.txt Error message: File not found\\n\"}";

    }

}