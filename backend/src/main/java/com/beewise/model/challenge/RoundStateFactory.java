package com.beewise.model.challenge;

public class RoundStateFactory {

    public static RoundState fromStatus(RoundStatus status) {
        return switch (status) {
            case COMPLETED -> new CompletedState();
            case WAITING_CHALLENGER -> new WaitingChallengerState();
            case WAITING_CHALLENGED -> new WaitingChallengedState();
        };
    }

    public static RoundStatus toStatus(RoundState state) {
        if (state instanceof CompletedState) return RoundStatus.COMPLETED;
        if (state instanceof WaitingChallengerState) return RoundStatus.WAITING_CHALLENGER;
        if (state instanceof WaitingChallengedState) return RoundStatus.WAITING_CHALLENGED;
        throw new IllegalArgumentException("Unknown RoundState: " + state.getClass());
    }
}
