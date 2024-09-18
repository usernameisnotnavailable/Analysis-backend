package dev.peter.Analysis.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
public class DataProviderControllerIntegrationTest {

    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    MockMvc mockMvc;

    @Value("${sql.create.script}")
    private String createScript;
    @Value("${sql.delete.script}")
    private String deleteScript;

    @BeforeEach
    public void setup(){
        jdbc.execute(createScript);
    }

    @Test
    public void requestThreeDaysOfStock() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.get("/stocks")
                .param("stock", "OTP")
                .param("from", "2024-05-01")
                .param("to", "2024-05-03"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    public void requestStockNotAvailable() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/stocks")
                .param("stock", "OTP")
                .param("from", "2024-05-07")
                .param("to", "2024-05-08"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void requestStockPartiallyAvailable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks")
                        .param("stock", "OTP")
                        .param("from", "2024-05-06")
                        .param("to", "2024-05-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }


    @AfterEach
    public void clear(){
        jdbc.execute(deleteScript);
    }

}
