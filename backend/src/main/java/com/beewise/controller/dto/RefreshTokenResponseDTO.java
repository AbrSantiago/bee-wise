package com.beewise.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponseDTO {
    public String accessTokenToken;
    public String refreshToken;

    public RefreshTokenResponseDTO() {

    }
}
