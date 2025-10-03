package com.beewise.model.challenge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundStateFactoryTest {

    @Test
    void fromStatus_withCompleted_returnsCompletedState() {
        RoundState state = RoundStateFactory.fromStatus(RoundStatus.COMPLETED);

        assertInstanceOf(CompletedState.class, state);
    }

    @Test
    void fromStatus_withWaitingChallenger_returnsWaitingChallengerState() {
        RoundState state = RoundStateFactory.fromStatus(RoundStatus.WAITING_CHALLENGER);

        assertInstanceOf(WaitingChallengerState.class, state);
    }

    @Test
    void fromStatus_withWaitingChallenged_returnsWaitingChallengedState() {
        RoundState state = RoundStateFactory.fromStatus(RoundStatus.WAITING_CHALLENGED);

        assertInstanceOf(WaitingChallengedState.class, state);
    }

    @Test
    void toStatus_withCompletedState_returnsCompleted() {
        CompletedState state = new CompletedState();

        RoundStatus status = RoundStateFactory.toStatus(state);

        assertEquals(RoundStatus.COMPLETED, status);
    }

    @Test
    void toStatus_withWaitingChallengerState_returnsWaitingChallenger() {
        WaitingChallengerState state = new WaitingChallengerState();

        RoundStatus status = RoundStateFactory.toStatus(state);

        assertEquals(RoundStatus.WAITING_CHALLENGER, status);
    }

    @Test
    void toStatus_withWaitingChallengedState_returnsWaitingChallenged() {
        WaitingChallengedState state = new WaitingChallengedState();

        RoundStatus status = RoundStateFactory.toStatus(state);

        assertEquals(RoundStatus.WAITING_CHALLENGED, status);
    }

    @Test
    void toStatus_withUnknownState_throwsIllegalArgumentException() {
        RoundState unknownState = new RoundState() {
            @Override
            public void answer(Challenge challenge, Round round, com.beewise.controller.dto.AnswerDTO answer) {
            }
        };

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RoundStateFactory.toStatus(unknownState);
        });

        assertTrue(exception.getMessage().contains("Unknown RoundState"));
    }

    @Test
    void fromStatus_toStatus_roundTrip_worksCorrectly() {
        for (RoundStatus status : RoundStatus.values()) {
            RoundState state = RoundStateFactory.fromStatus(status);
            RoundStatus convertedBack = RoundStateFactory.toStatus(state);

            assertEquals(status, convertedBack);
        }
    }
}