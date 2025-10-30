package com.beewise.repository;

import com.beewise.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    Optional<Reward> findByChallengeIdAndUserId(Long challengeId, Long userId);

    boolean existsByChallengeId(Long challengeId);
}