package com.beewise.controller.dto;

import com.beewise.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpponentDTO {
    private String username;
    public OpponentDTO(User user) {
        this.username = user.getUsername();
    }
}
