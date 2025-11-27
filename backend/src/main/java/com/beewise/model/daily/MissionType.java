package com.beewise.model.daily;

import java.util.concurrent.ThreadLocalRandom;

public enum MissionType {
    PLAY_CHALLENGE,
    WIN_CHALLENGE,
    SPEND_BEECOINS,
    EARN_POINTS,
    COMPLETE_LESSON,
    CORRECT_EXCERSICES,
    BYE_ITEMS;

    public int getGoalAmount() {
        return switch (this) {
            case PLAY_CHALLENGE, COMPLETE_LESSON, BYE_ITEMS -> 2;
            case WIN_CHALLENGE -> 1;
            case SPEND_BEECOINS -> 40;
            case EARN_POINTS -> 100;
            case CORRECT_EXCERSICES -> 8;
        };
    }

    public static MissionType[] getTwoRandomDifferent() {
        MissionType[] values = MissionType.values();
        MissionType first = values[ThreadLocalRandom.current().nextInt(values.length)];

        MissionType second;
        do {
            second = values[ThreadLocalRandom.current().nextInt(values.length)];
        } while (second == first);

        return new MissionType[]{first, second};
    }
}
