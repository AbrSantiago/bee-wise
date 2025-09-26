package com.beewise.repository;

import com.beewise.model.challenge.Challenge;
import com.beewise.model.challenge.ChallengeStatus;
import com.beewise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    boolean existsByChallengerAndChallengedAndStatusIn(User challenger, User challenged, List<ChallengeStatus> statuses);
}
