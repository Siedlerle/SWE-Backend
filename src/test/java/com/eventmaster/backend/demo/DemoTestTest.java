package com.eventmaster.backend.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoTestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDemoEndpoint() throws Exception {
        mockMvc.perform(get("/demo"))
                .andExpect(status().isOk())
                .andExpect(content().string("successful test"));
    }
}