package com.beewise.controller;

import com.beewise.controller.dto.LessonDTO;
import com.beewise.model.Lesson;
import com.beewise.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id){
        Lesson lesson = lessonService.getLessonById(id);
        LessonDTO lessonDTO = LessonDTO.fromLesson(lesson);
        return ResponseEntity.ok(lessonDTO);
    }
}
