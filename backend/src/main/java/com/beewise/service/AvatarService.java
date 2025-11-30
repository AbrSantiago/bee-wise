package com.beewise.service;

import com.beewise.controller.dto.AvatarDTO;
import com.beewise.model.Avatar;
import com.beewise.model.User;

public interface AvatarService {
    Avatar newDefaultAvatar(User user);
    Avatar updateAvatar(Long id, AvatarDTO dto);
}
