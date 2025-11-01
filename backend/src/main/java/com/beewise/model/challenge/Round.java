package com.beewise.model.challenge;

import com.beewise.controller.dto.AnswerDTO;
import com.beewise.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    private int roundNumber;

    private int challengerScore = 0;

    private int challengedScore = 0;

    @Enumerated(EnumType.STRING)
    private RoundStatus status;

    @Column(name = "challenger_correct_answers")
    private int challengerCorrectAnswers = 0;

    @Column(name = "challenged_correct_answers")
    private int challengedCorrectAnswers = 0;

    public Round(Challenge challenge, int roundNumber, RoundStatus status) {
        this.challenge = challenge;
        this.roundNumber = roundNumber;
        this.status = status;
    }

    public User winner() {
        if (challengerScore > challengedScore) {
            return challenge.getChallenger();
        } else if (challengerScore <= challengedScore){
            return challenge.getChallenged();
        } else {
            return null;
        }
    }

    public void answer(AnswerDTO answer) {
        getState().answer(challenge, this, answer);
    }

    public RoundState getState() {
        return RoundStateFactory.fromStatus(this.status);
    }

    public boolean isCompleted() {
        return status == RoundStatus.COMPLETED;
    }

    public boolean isWaitingChallenger() {
        return status == RoundStatus.WAITING_CHALLENGER;
    }

    public boolean isWaitingChallenged() {
        return status == RoundStatus.WAITING_CHALLENGED;
    }
    public boolean isChallengerPerfectRound() {
        return challengerCorrectAnswers == challenge.getQuestionsPerRound();
    }

    public boolean isChallengedPerfectRound() {
        return challengedCorrectAnswers == challenge.getQuestionsPerRound();
    }

}
