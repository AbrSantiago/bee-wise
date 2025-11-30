package com.beewise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<String> handleLessonNotFoundException(LessonNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LessonAlreadyExistsException.class)
    public ResponseEntity<String> handleLessonAlreadyExistsException(LessonAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<String> handleExerciseNotFoundException(ExerciseNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InvalidIdException.class,
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class,
            UserNotFoundException.class,
            ChallengeAlreadyExistsException.class,
            ChallengeNotFoundException.class,
            ChallengeNotPendingException.class,
            RoundNumberException.class,
            AnswerWrongRolException.class,
            RoundCompletedException.class,
            WaitingFotChallengerException.class,
            UserChallengesHimselfException.class,
            AnswerNotAllowedException.class,
            InvalidTokenException.class,
            ChallengeAlreadyCompletedException.class,
            AvatarDoesNotExistsException.class,
            ShopItemDoesNotExistsException.class,
            SomeItemsWereNotBought.class,
            ItemAlreadyBoughtException.class,
            NotEnoughBeeCoinsException.class,
            UserNotPlayingChallengeException.class,
            UserAlreadyGotRewardException.class,
            ChallengeNotCompleteYetException.class,
            MissionDoesNotExistException.class,
            MissionProgressDoesNotExistException.class
    })
    public ResponseEntity<Map<String, String>> handleBadRequestExceptions(RuntimeException ex) {
        Map<String, String> error = Map.of("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
