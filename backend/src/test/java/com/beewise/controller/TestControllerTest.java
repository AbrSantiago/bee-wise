package com.beewise.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
class TestControllerTest {

    @Autowired
    private final MockMvc mockMvc;

    TestControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void helloEndpoint_ShouldReturnGreeting() throws Exception {
        mockMvc.perform(get("/test/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Hola desde el backend de Bee-Wise! 🐝"));
    }

    @Test
    void statusEndpoint_ShouldReturnValidJsonResponse() throws Exception {
        mockMvc.perform(get("/test/status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Backend funcionando correctamente"))
                .andExpect(jsonPath("$.service").value("Bee-Wise API"))
                // verificamos que timestamp exista
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void corsEndpoint_ShouldReturnCorsMessage() throws Exception {
        mockMvc.perform(get("/test/cors"))
                .andExpect(status().isOk())
                .andExpect(content().string("CORS configurado correctamente 🌐"));
    }

    @Test
    void pingEndpoint_ShouldReturnPong() throws Exception {
        mockMvc.perform(get("/test/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}
