package com.beewise.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeRolTest {

    @Test
    void enum_hasCorrectValues() {
        ChallengeRol[] values = ChallengeRol.values();

        assertEquals(2, values.length);
        assertEquals(ChallengeRol.CHALLENGER, values[0]);
        assertEquals(ChallengeRol.CHALLENGED, values[1]);
    }

    @Test
    void valueOf_withValidNames_returnsCorrectEnum() {
        assertEquals(ChallengeRol.CHALLENGER, ChallengeRol.valueOf("CHALLENGER"));
        assertEquals(ChallengeRol.CHALLENGED, ChallengeRol.valueOf("CHALLENGED"));
    }

    @Test
    void valueOf_withInvalidName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ChallengeRol.valueOf("INVALID");
        });
    }

    @Test
    void values_returnsSameArray() {
        ChallengeRol[] values1 = ChallengeRol.values();
        ChallengeRol[] values2 = ChallengeRol.values();

        assertArrayEquals(values1, values2);
    }

    @Test
    void name_returnsCorrectString() {
        assertEquals("CHALLENGER", ChallengeRol.CHALLENGER.name());
        assertEquals("CHALLENGED", ChallengeRol.CHALLENGED.name());
    }

    @Test
    void ordinal_returnsCorrectPosition() {
        assertEquals(0, ChallengeRol.CHALLENGER.ordinal());
        assertEquals(1, ChallengeRol.CHALLENGED.ordinal());
    }

    @Test
    void toString_returnsCorrectString() {
        assertEquals("CHALLENGER", ChallengeRol.CHALLENGER.toString());
        assertEquals("CHALLENGED", ChallengeRol.CHALLENGED.toString());
    }

    @Test
    void equals_worksCorrectly() {
        assertTrue(ChallengeRol.CHALLENGER.equals(ChallengeRol.CHALLENGER));
        assertTrue(ChallengeRol.CHALLENGED.equals(ChallengeRol.CHALLENGED));
        assertFalse(ChallengeRol.CHALLENGER.equals(ChallengeRol.CHALLENGED));
        assertFalse(ChallengeRol.CHALLENGED.equals(ChallengeRol.CHALLENGER));
        assertFalse(ChallengeRol.CHALLENGER.equals(null));
        assertFalse(ChallengeRol.CHALLENGER.equals("CHALLENGER"));
    }

    @Test
    void hashCode_isConsistent() {
        assertEquals(ChallengeRol.CHALLENGER.hashCode(), ChallengeRol.CHALLENGER.hashCode());
        assertEquals(ChallengeRol.CHALLENGED.hashCode(), ChallengeRol.CHALLENGED.hashCode());
    }
}