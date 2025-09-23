package com.beewise.controller;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.controller.dto.ChallengeDTO;
import com.beewise.controller.dto.RoundDTO;
import com.beewise.controller.dto.SendChallengeDTO;
import com.beewise.model.Challenge;
import com.beewise.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
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

    @PostMapping("/{challengeId}/round/{round}/answer")
    public ResponseEntity<ChallengeDTO> answerRound(@RequestBody AnswerDTO answer) {
        Challenge challenge = challengeService.answerRound(answer);
        ChallengeDTO dto = ChallengeDTO.fromChallenge(challenge);
        return ResponseEntity.ok(dto);
    }
}
