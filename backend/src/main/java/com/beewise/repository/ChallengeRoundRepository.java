package com.beewise.repository;

import com.beewise.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRoundRepository extends JpaRepository<Round, Long> {}
