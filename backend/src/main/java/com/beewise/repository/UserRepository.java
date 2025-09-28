package com.beewise.repository;

import com.beewise.model.User;
import com.beewise.model.challenge.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    @Query("""
        SELECT u FROM User u
        WHERE u.id <> :challengerId
        AND NOT EXISTS (
            SELECT c FROM Challenge c
            WHERE (
                (c.challenger.id = :challengerId AND c.challenged.id = u.id)
                OR (c.challenger.id = u.id AND c.challenged.id = :challengerId)
            )
            AND c.status IN :activeStatuses
        )
    """)
    List<User> findAvailableToChallenge(
            @Param("challengerId") Long challengerId,
            @Param("activeStatuses") List<ChallengeStatus> activeStatuses
    );
}
