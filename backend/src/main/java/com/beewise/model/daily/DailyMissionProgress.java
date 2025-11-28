package com.beewise.model.daily;

import com.beewise.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DailyMissionProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private DailyMission mission;

    @Min(value = 0, message = "¨Progress can not be negative")
    private int currentProgress = 0;

    private boolean isClaimed = false;

    public void updateProgress(int delta){
        this.currentProgress = Math.min(this.currentProgress + delta, this.mission.getGoalAmount());
    }

    public boolean isCompleted(){
        return this.currentProgress == this.mission.getGoalAmount();
    }

    public int getReward() {
        return this.mission.getRewardAmount();
    }
}
