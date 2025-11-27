package com.beewise.controller;

import com.beewise.controller.dto.DailyMissionDTO;
import com.beewise.exception.AnswerWrongRolException;
import com.beewise.exception.MissionAlreadyClaimedException;
import com.beewise.exception.MissionNotCompletedException;
import com.beewise.model.daily.DailyMission;
import com.beewise.model.daily.DailyMissionProgress;
import com.beewise.service.DailyMissionService;
import com.beewise.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/dailyMissions")
public class DailyMissionController {

    private final DailyMissionService dailyMissionService;
    private final JwtService jwtService;

    public DailyMissionController(DailyMissionService dailyMissionService, JwtService jwtService) {
        this.dailyMissionService = dailyMissionService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<DailyMissionDTO>> getChallengeSummary(
            @RequestHeader("Authorization") String authHeader
    ) {
        String username = jwtService.extractUsername(authHeader.substring(7));
        List<DailyMissionProgress> missions = dailyMissionService.getDailyMissions(username);
        return ResponseEntity.ok(missions.stream().map(DailyMissionDTO::new).toList());
    }


//    @PostMapping("/{id}/claim")
//    public ResponseEntity<DailyMissionDTO> claimReward(@PathVariable Long id, Principal principal) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        try {
//            DailyMission updatedMission = dailyMissionService.claimReward(id, principal.getName());
//            return ResponseEntity.ok(new DailyMissionDTO(updatedMission)); // Asumiendo que tienes un DTO
//
//        } catch (MissionNotCompletedException | MissionAlreadyClaimedException e) {
//            return ResponseEntity.badRequest().body(null); // O un DTO de error
//        } catch (AnswerWrongRolException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//    }
}
