package com.beewise.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void helloEndpoint_ShouldReturnGreeting() throws Exception {
        mockMvc.perform(get("/test/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from the Bee-Wise backend! \uD83D\uDC1D"));
    }

    @Test
    @WithMockUser
    void statusEndpoint_ShouldReturnValidJsonResponse() throws Exception {
        mockMvc.perform(get("/test/status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Backend is running properly"))
                .andExpect(jsonPath("$.service").value("Bee-Wise API"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser
    void corsEndpoint_ShouldReturnCorsMessage() throws Exception {
        mockMvc.perform(get("/test/cors"))
                .andExpect(status().isOk())
                .andExpect(content().string("CORS configured correctly \uD83C\uDF10"));
    }

    @Test
    @WithMockUser
    void pingEndpoint_ShouldReturnPong() throws Exception {
        mockMvc.perform(get("/test/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}
