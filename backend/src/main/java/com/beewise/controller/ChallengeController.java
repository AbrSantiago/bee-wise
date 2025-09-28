package com.beewise.controller;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeDTO;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.controller.dto.UserDTO;
import com.beewise.model.User;
import com.beewise.model.challenge.Challenge;
import com.beewise.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAll() {
        List<Challenge> challenges = challengeService.getAll();
        List<ChallengeDTO> challengeDTOS = challenges.stream().map(ChallengeDTO::new).toList();
        return ResponseEntity.ok(challengeDTOS);
    }

    @GetMapping("/usersToChallenge/{challengerId}")
    public ResponseEntity<List<UserDTO>> getUsersToChallenge(@PathVariable Long challengerId) {
        List<User> users = challengeService.getUsersToChallenge(challengerId);
        List<UserDTO> useDTOs = users.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok(useDTOs);
    }

    @PostMapping("/send")
    public ResponseEntity<ChallengeDTO> sendChallenge(@Valid @RequestBody SendChallengeDTO challengeDTO) {
        Challenge challenge = challengeService.sendChallenge(challengeDTO);
        ChallengeDTO dto = ChallengeDTO.fromChallenge(challenge);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/accept/{challengeId}")
    public ResponseEntity<ChallengeDTO> acceptChallenge(@PathVariable Long challengeId) {
        Challenge challenge = challengeService.acceptChallenge(challengeId);
        ChallengeDTO dto = ChallengeDTO.fromChallenge(challenge);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/answer")
    public ResponseEntity<ChallengeDTO> answerRound(@Valid @RequestBody AnswerDTO answer) {
        Challenge challenge = challengeService.answerRound(answer);
        ChallengeDTO dto = ChallengeDTO.fromChallenge(challenge);
        return ResponseEntity.ok(dto);
    }
}
