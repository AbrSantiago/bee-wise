package com.beewise.controller;

import com.beewise.controller.dto.SimpleLessonDTO;
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

    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@RequestBody SimpleLessonDTO simpleLessonDTO){
        Lesson lesson = lessonService.createLesson(simpleLessonDTO);
        LessonDTO lessonDTO = LessonDTO.fromLesson(lesson);
        return ResponseEntity.ok(lessonDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(
            @PathVariable Long id,
            @RequestBody SimpleLessonDTO simpleLessonDTO
    ){
        Lesson lesson = lessonService.updateLesson(id, simpleLessonDTO);
        LessonDTO lessonDTO = LessonDTO.fromLesson(lesson);
        return ResponseEntity.ok(lessonDTO);
    }

    @PutMapping("/{lessonId}/add exercise/{exerciseId}")
    public ResponseEntity<LessonDTO> addExercise(
            @PathVariable Long lessonId,
            @PathVariable Long exerciseId
    ){
        Lesson lesson = lessonService.addExercise(lessonId, exerciseId);
        LessonDTO lessonDTO = LessonDTO.fromLesson(lesson);
        return ResponseEntity.ok(lessonDTO);
    }

    @PutMapping("/{lessonId}/remove exercise/{exerciseId}")
    public ResponseEntity<LessonDTO> removeExercise(
            @PathVariable Long lessonId,
            @PathVariable Long exerciseId
    ){
        Lesson lesson = lessonService.removeExercise(lessonId, exerciseId);
        LessonDTO lessonDTO = LessonDTO.fromLesson(lesson);
        return ResponseEntity.ok(lessonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id){
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
