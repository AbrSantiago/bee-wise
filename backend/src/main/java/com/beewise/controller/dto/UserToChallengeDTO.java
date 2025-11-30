package com.beewise.controller.dto;

import com.beewise.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserToChallengeDTO {
    private Long id;
    private String username;
    private AvatarDTO avatar;
    private int points;

    public UserToChallengeDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatar = new AvatarDTO(user.getAvatar());
        this.points = user.getPoints();
    }
}
