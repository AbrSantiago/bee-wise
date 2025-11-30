package com.beewise.controller;

import com.beewise.controller.dto.DailyMissionDTO;
import com.beewise.controller.dto.DailyMissionUpdateDTO;
import com.beewise.controller.dto.DailyMissionUpdateOutDTO;
import com.beewise.model.daily.DailyMissionProgress;
import com.beewise.service.DailyMissionService;
import com.beewise.service.impl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<DailyMissionDTO>> getDailyMissions(
            @RequestHeader("Authorization") String authHeader
    ) {
        String username = jwtService.extractUsername(authHeader.substring(7));
        List<DailyMissionProgress> missions = dailyMissionService.getDailyMissions(username);
        return ResponseEntity.ok(missions.stream().map(DailyMissionDTO::new).toList());
    }

    @PutMapping("/update")
    public ResponseEntity<List<DailyMissionUpdateOutDTO>> updateProgress(
            @RequestBody List<DailyMissionUpdateDTO> dailyMissionUpdateDTOS,
            @RequestHeader("Authorization") String authHeader
    ) {
        String username = jwtService.extractUsername(authHeader.substring(7));

        List<DailyMissionUpdateOutDTO> missions = dailyMissionService.updateProgress(username, dailyMissionUpdateDTOS);
        return ResponseEntity.ok(missions);
    }
}
