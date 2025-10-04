package com.beewise.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TestPrueba {

    @Test // ✅ Agregar esta anotación
    void testBasico() {
        assertTrue(true); // Test simple que siempre pasa
    }

    @Test
    void testDummy() {
        // Test vacío para probar
        System.out.println("Test ejecutado correctamente");
    }
}