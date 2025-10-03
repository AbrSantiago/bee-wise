package com.beewise.controller.dto;

import com.beewise.model.Lesson;
import com.beewise.model.LessonProgress;
import com.beewise.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void noArgsConstructor_createsEmptyObject() {
        UserDTO dto = new UserDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getSurname());
        assertNull(dto.getEmail());
        assertNull(dto.getUsername());
        assertEquals(0, dto.getPoints());
        assertNull(dto.getCompletedLessons());
        assertEquals(0, dto.getCurrentLesson());
    }

    @Test
    void constructor_mapsAllFieldsCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPoints(150);
        user.setCurrentLesson(5);

        Lesson lesson1 = new Lesson();
        lesson1.setId(10L);
        Lesson lesson2 = new Lesson();
        lesson2.setId(20L);

        LessonProgress progress1 = new LessonProgress();
        progress1.setLesson(lesson1);
        LessonProgress progress2 = new LessonProgress();
        progress2.setLesson(lesson2);

        user.setLessonProgresses(List.of(progress1, progress2));

        UserDTO dto = new UserDTO(user);

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("Doe", dto.getSurname());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("johndoe", dto.getUsername());
        assertEquals(150, dto.getPoints());
        assertEquals(5, dto.getCurrentLesson());
        assertEquals(2, dto.getCompletedLessons().size());
        assertTrue(dto.getCompletedLessons().contains(10L));
        assertTrue(dto.getCompletedLessons().contains(20L));
    }

    @Test
    void constructor_withNullFields_handlesCorrectly() {
        User user = new User();
        user.setId(null);
        user.setName(null);
        user.setSurname(null);
        user.setEmail(null);
        user.setUsername(null);
        user.setPoints(0);
        user.setCurrentLesson(0);
        user.setLessonProgresses(new ArrayList<>());

        UserDTO dto = new UserDTO(user);

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getSurname());
        assertNull(dto.getEmail());
        assertNull(dto.getUsername());
        assertEquals(0, dto.getPoints());
        assertEquals(0, dto.getCurrentLesson());
        assertTrue(dto.getCompletedLessons().isEmpty());
    }

    @Test
    void constructor_withEmptyLessonProgresses_returnsEmptyList() {
        User user = new User();
        user.setId(2L);
        user.setName("Jane");
        user.setSurname("Smith");
        user.setEmail("jane.smith@example.com");
        user.setUsername("janesmith");
        user.setPoints(75);
        user.setCurrentLesson(3);
        user.setLessonProgresses(new ArrayList<>());

        UserDTO dto = new UserDTO(user);

        assertEquals(2L, dto.getId());
        assertEquals("Jane", dto.getName());
        assertEquals("Smith", dto.getSurname());
        assertEquals("jane.smith@example.com", dto.getEmail());
        assertEquals("janesmith", dto.getUsername());
        assertEquals(75, dto.getPoints());
        assertEquals(3, dto.getCurrentLesson());
        assertTrue(dto.getCompletedLessons().isEmpty());
    }

    @Test
    void constructor_withMultipleLessonProgresses_mapsAllIds() {
        User user = new User();
        user.setId(3L);
        user.setName("Bob");
        user.setSurname("Wilson");
        user.setEmail("bob.wilson@example.com");
        user.setUsername("bobwilson");
        user.setPoints(200);
        user.setCurrentLesson(8);

        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        Lesson lesson3 = new Lesson();
        lesson3.setId(3L);

        LessonProgress progress1 = new LessonProgress();
        progress1.setLesson(lesson1);
        LessonProgress progress2 = new LessonProgress();
        progress2.setLesson(lesson2);
        LessonProgress progress3 = new LessonProgress();
        progress3.setLesson(lesson3);

        user.setLessonProgresses(List.of(progress1, progress2, progress3));

        UserDTO dto = new UserDTO(user);

        assertEquals(3, dto.getCompletedLessons().size());
        assertTrue(dto.getCompletedLessons().contains(1L));
        assertTrue(dto.getCompletedLessons().contains(2L));
        assertTrue(dto.getCompletedLessons().contains(3L));
    }

    @Test
    void setters_workCorrectly() {
        UserDTO dto = new UserDTO();

        dto.setId(100L);
        assertEquals(100L, dto.getId());

        dto.setName("Alice");
        assertEquals("Alice", dto.getName());

        dto.setSurname("Johnson");
        assertEquals("Johnson", dto.getSurname());

        dto.setEmail("alice@example.com");
        assertEquals("alice@example.com", dto.getEmail());

        dto.setUsername("alicej");
        assertEquals("alicej", dto.getUsername());

        dto.setPoints(300);
        assertEquals(300, dto.getPoints());

        dto.setCurrentLesson(10);
        assertEquals(10, dto.getCurrentLesson());

        List<Long> lessonIds = List.of(1L, 2L, 3L);
        dto.setCompletedLessons(lessonIds);
        assertEquals(lessonIds, dto.getCompletedLessons());
    }

    @Test
    void getters_workCorrectly() {
        UserDTO dto = new UserDTO();
        dto.setId(50L);
        dto.setName("Charlie");
        dto.setSurname("Brown");
        dto.setEmail("charlie@example.com");
        dto.setUsername("charlieb");
        dto.setPoints(125);
        dto.setCurrentLesson(7);
        dto.setCompletedLessons(List.of(5L, 6L));

        assertEquals(50L, dto.getId());
        assertEquals("Charlie", dto.getName());
        assertEquals("Brown", dto.getSurname());
        assertEquals("charlie@example.com", dto.getEmail());
        assertEquals("charlieb", dto.getUsername());
        assertEquals(125, dto.getPoints());
        assertEquals(7, dto.getCurrentLesson());
        assertEquals(2, dto.getCompletedLessons().size());
    }

    @Test
    void constructor_withZeroPoints_handlesCorrectly() {
        User user = new User();
        user.setId(4L);
        user.setName("David");
        user.setSurname("Lee");
        user.setEmail("david.lee@example.com");
        user.setUsername("davidlee");
        user.setPoints(0);
        user.setCurrentLesson(1);
        user.setLessonProgresses(new ArrayList<>());

        UserDTO dto = new UserDTO(user);

        assertEquals(0, dto.getPoints());
        assertEquals(1, dto.getCurrentLesson());
    }

    @Test
    void constructor_withNegativePoints_handlesCorrectly() {
        User user = new User();
        user.setId(5L);
        user.setName("Eva");
        user.setSurname("Martinez");
        user.setEmail("eva.martinez@example.com");
        user.setUsername("evam");
        user.setPoints(-10);
        user.setCurrentLesson(0);
        user.setLessonProgresses(new ArrayList<>());

        UserDTO dto = new UserDTO(user);

        assertEquals(-10, dto.getPoints());
        assertEquals(0, dto.getCurrentLesson());
    }
}