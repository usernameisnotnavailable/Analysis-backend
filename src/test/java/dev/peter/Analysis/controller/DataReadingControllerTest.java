package dev.peter.Analysis.controller;

import dev.peter.Analysis.services.datainput.StockInputService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DataReadingController.class)
class DataReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockInputService stockInputService;

    @Test
    void testReadDataNoWithoutPath() throws Exception {
        String requestBody = "[]";
        String customResponse = "No path provided in the request for /read-data";
        mockMvc.perform(post("/read-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(customResponse)));

        verify(stockInputService, never()).read(anyString());
    }

    @Test
    void testReadDataWithValidPath() throws Exception {
        String requestBody = "[\"/mock/test/path.txt\"]";

        String customResponse = "File read from: /mock/test/path.txt";

        mockMvc.perform(post("/read-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(customResponse)));

        verify(stockInputService).read("/mock/test/path.txt");
    }

    @Test
    void testReadDataWithFileNotFoundException() throws Exception {
        String requestBody = "[\"/mock/test/path.txt\"]";

        doThrow(new FileNotFoundException("File not found")).when(stockInputService).read("/mock/test/path.txt");

        mockMvc.perform(post("/read-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Request successfully processed")))
                .andExpect(jsonPath(
                        "$.message",
                        is("Error on path: /mock/test/path.txt Error message: java.io.FileNotFoundException: File not found")))
        ;

    }

}