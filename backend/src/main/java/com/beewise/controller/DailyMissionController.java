package com.beewise.controller;

import com.beewise.exception.AnswerWrongRolException;
import com.beewise.exception.MissionAlreadyClaimedException;
import com.beewise.exception.MissionNotCompletedException;
import com.beewise.model.daily.DailyMission;
import com.beewise.service.DailyMissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/daily-missions")
public class DailyMissionController {

    private final DailyMissionService dailyMissionService;

    public DailyMissionController(DailyMissionService dailyMissionService) {
        this.dailyMissionService = dailyMissionService;
    }

    @PostMapping("/{id}/claim")
    public ResponseEntity<DailyMissionDTO> claimReward(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            DailyMission updatedMission = dailyMissionService.claimReward(id, principal.getName());
            return ResponseEntity.ok(new DailyMissionDTO(updatedMission)); // Asumiendo que tienes un DTO

        } catch (MissionNotCompletedException | MissionAlreadyClaimedException e) {
            return ResponseEntity.badRequest().body(null); // O un DTO de error
        } catch (AnswerWrongRolException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


}
