package com.beewise.service;

import com.beewise.controller.dto.*;
import com.beewise.model.ItemCategory;
import com.beewise.model.ShopItem;
import com.beewise.model.User;
import com.beewise.model.challenge.ChallengeStatus;

import java.util.List;
import java.util.Map;

public interface UserService {
    User registerUser(RegisterUserDTO registerUserDTO);
    User authenticateUser(LoginUserDTO loginUserDTO);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    LessonCompleteDTO lessonComplete(LessonCompleteRequestDTO requestDTO);
    UserPointsDTO getUserPoints(String username);
    List<User> getUsersToChallenge(Long challengerId, List<ChallengeStatus> activeStatuses);
    User updateAvatar(Long id, AvatarDTO avatarDTO);
    User buyItem(Long itemId, String username);
    List<ShopItem> getUserItems(String username);
    Map<ItemCategory, List<ShopItem>> getAllByCategory(String username);
    Boolean addPointsToUser(User user, Integer pointsToAdd);
}
