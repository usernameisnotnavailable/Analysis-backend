package dev.peter.Analysis.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    MockMvc mockMvc;
    @BeforeAll
    void init() {
        jdbc.execute(createTable);
        jdbc.execute(createScript);
    }

    @AfterAll
    void clear() {
        jdbc.execute(deleteScript);
        jdbc.execute("drop table stock");
    }

    @Value("${sql.create.table.script}")
    private String createTable;

    @Value("${sql.create.script}")
    private String createScript;
    @Value("${sql.delete.script}")
    private String deleteScript;


    @Test
    public void companyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/getStockList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("otp"));


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
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks")
                .param("stock", "OTP")
                .param("from", "2024-05-07")
                .param("to", "2024-05-08"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

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




}
