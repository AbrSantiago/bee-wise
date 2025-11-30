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

    /** Desafíos jugados (en cualquier rol) */
    @Query("""
        SELECT COUNT(c)
        FROM Challenge c
        WHERE c.challenger.id = :userId OR c.challenged.id = :userId
    """)
    int getChallengesPlayed(@Param("userId") Long userId);

    /** Desafíos ganados */
    @Query("""
        SELECT COUNT(c)
        FROM Challenge c
        WHERE c.result = 'CHALLENGER_WIN' AND c.challenger.id = :userId
           OR c.result = 'CHALLENGED_WIN' AND c.challenged.id = :userId
    """)
    int getChallengesWon(@Param("userId") Long userId);

    /** Rondas ganadas */
    @Query("""
        SELECT COUNT(r)
        FROM Round r
        WHERE (r.challenge.challenger.id = :userId AND r.challengerScore > r.challengedScore)
           OR (r.challenge.challenged.id = :userId AND r.challengedScore > r.challengerScore)
    """)
    int getRoundsWon(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(
            AVG(
                CASE
                    WHEN r.challenge.challenger.id = :userId THEN r.challengerScore * 1.0 / r.challenge.questionsPerRound
                    ELSE r.challengedScore * 1.0 / r.challenge.questionsPerRound
                END
            ) * 100, 0)
        FROM Round r
    """)
    double getAccuracyPercentagePerChallenge(@Param("userId") Long userId);

    /** Precisión general (respuestas correctas / total de respuestas) */
    @Query("""
        SELECT COALESCE(
            SUM(CASE\s
                    WHEN r.challenge.challenger.id = :userId THEN r.challengerScore
                    ELSE r.challengedScore
                END) * 1.0 /\s
            SUM(r.challenge.questionsPerRound), 0)
        FROM Round r
    \s""")
    double getAccuracy(@Param("userId") Long userId);
}
