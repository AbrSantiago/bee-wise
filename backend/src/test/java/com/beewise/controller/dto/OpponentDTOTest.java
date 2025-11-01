package com.beewise.controller.dto;

import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OpponentDTOTest {

    @Test
    void constructor_setsUsernameCorrectly() {
        User user = new User();
        user.setUsername("testuser");

        OpponentDTO dto = new OpponentDTO(user);

        assertEquals("testuser", dto.getUsername());
    }

    @Test
    void noArgsConstructor_setsUsernameNull() {
        OpponentDTO dto = new OpponentDTO();
        assertNull(dto.getUsername());
    }

    @Test
    void setUsername_setsUsernameCorrectly() {
        OpponentDTO dto = new OpponentDTO();
        dto.setUsername("anotheruser");
    }
}