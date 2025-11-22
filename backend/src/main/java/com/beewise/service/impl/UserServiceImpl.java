package com.beewise.service.impl;

import com.beewise.controller.dto.*;
import com.beewise.exception.*;
import com.beewise.model.*;
import com.beewise.model.challenge.ChallengeStatus;
import com.beewise.repository.LevelRepository;
import com.beewise.repository.UserRepository;
import com.beewise.service.*;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LessonProgressService progressService;
    private final LessonService lessonService;
    private final AvatarService avatarService;
    private final ShopService shopService;
    private final LevelRepository levelRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            LessonProgressService progressService,
            LessonService lessonService,
            AvatarService avatarService, ShopService shopService, LevelRepository levelRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.progressService = progressService;
        this.lessonService = lessonService;
        this.avatarService = avatarService;
        this.shopService = shopService;
        this.levelRepository = levelRepository;
    }

    @Override
    public User registerUser(RegisterUserDTO registerUserDTO) {
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(registerUserDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User();
        newUser.setName(registerUserDTO.getName());
        newUser.setSurname(registerUserDTO.getSurname());
        newUser.setEmail(registerUserDTO.getEmail());
        newUser.setUsername(registerUserDTO.getUsername());
        newUser.setPasswordHash(passwordEncoder.encode(registerUserDTO.getPassword()));

        Level defaultLevel = levelRepository.findFirstByOrderByMinPointsAsc()
                .orElseThrow(() -> new RuntimeException("Not found level 1 in database."));
        newUser.setLevel(defaultLevel);

        User savedUser = userRepository.save(newUser);

        Avatar avatar = avatarService.newDefaultAvatar(savedUser);

        savedUser.setAvatar(avatar);

        return savedUser;
    }

    @Override
    public User authenticateUser(LoginUserDTO loginUserDTO) {
        User user = userRepository.findByUsername(loginUserDTO.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(loginUserDTO.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public LessonCompleteDTO lessonComplete(LessonCompleteRequestDTO requestDTO) {
        Lesson lesson = lessonService.getLessonById(requestDTO.getCompletedLessonId());
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Level oldLevel = user.getLevel();

        Integer pointsEarned = requestDTO.getCorrectExercises() * 10;
        addPointsToUser(user, pointsEarned);
        user.setCurrentLesson(user.getCurrentLesson() + 1);
        userRepository.save(user);
        progressService.upsertProgress(user, lesson);

        LevelUpInfoDTO levelUpInfo = null;
        if (!oldLevel.getId().equals(user.getLevel().getId())) {
            levelUpInfo = new LevelUpInfoDTO(
                    oldLevel.getId(),
                    user.getLevel().getId(),
                    user.getLevel().getName(),
                    user.getLevel().getIconUrl()
            );
        }

        return new LessonCompleteDTO(true, "Progress updated", user.getPoints(), levelUpInfo);
    }

    @Override
    public UserPointsDTO getUserPoints(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserPointsDTO(user.getId(), user.getUsername(), user.getPoints(), user.getCurrentLesson());
    }

    @Override
    public List<User> getUsersToChallenge(Long challengerId, List<ChallengeStatus> activeStatuses) {
        return userRepository.findAvailableToChallenge(challengerId, activeStatuses);
    }

    @Override
    public User updateAvatar(Long id, AvatarDTO avatarDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        checkIfHasAllItems(avatarDTO, user);
        Avatar avatar = avatarService.updateAvatar(user.getAvatar().getId(), avatarDTO);
        user.setAvatar(avatar);
        return userRepository.save(user);
    }

    @Override
    public User buyItem(Long itemId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        ShopItem item = shopService.getItem(itemId);
        List<ShopItem> userItems = user.getItems();
        if (userItems.contains(item)) {
            throw new ItemAlreadyBoughtException("Item " + itemId + " was already bought by " + username);
        }
        if (user.getBeeCoins() < item.getPrice()) {
            throw new NotEnoughBeeCoinsException("User " + username + " has obtain not enough BeeCoins to buy item " + itemId);
        }
        user.spendBeeCoins(item.getPrice());
        userItems.add(item);
        user.setItems(userItems);
        return userRepository.save(user);
    }

    @Override
    public List<ShopItem> getUserItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return user.getItems();
    }

    @Override
    public Map<ItemCategory, List<ShopItem>> getAllByCategory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        List<ShopItem> items = user.getItems();
        items.addAll(shopService.getFreeItems());
        return items.stream().collect(Collectors.groupingBy(ShopItem::getCategory));
    }

    @Override
    public Boolean addPointsToUser(User user, Integer pointsToAdd) {
        if (pointsToAdd <= 0) return false;

        user.addPoints(pointsToAdd);

        checkAndUpdateUserLevel(user);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserStatsDTO getStats(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        Long userId = user.getId();
        UserStatsDTO statsDTO = new UserStatsDTO();
        statsDTO.setChallengesPlayed(userRepository.getChallengesPlayed(userId));
        statsDTO.setChallengesWon(userRepository.getChallengesWon(userId));
        statsDTO.setWinRate(statsDTO.getChallengesPlayed() == 0 ? 0 :
                100 * userRepository.getChallengesWon(userId) / userRepository.getChallengesPlayed(userId));
        statsDTO.setRoundsWon(userRepository.getRoundsWon(userId));
        statsDTO.setAvgCorrectAnswersPerChallenge((int) userRepository.getAccuracyPercentagePerChallenge(userId));
        statsDTO.setTotalPoints(user.getPoints());
        statsDTO.setBeeCoinsSpent(user.getSpentBeeCoins());
        statsDTO.setLongestWinStreak(11);
        statsDTO.setItemsObtained(user.getItems().size());
        statsDTO.setPercentile(12);
        statsDTO.setAccuracy((int) userRepository.getAccuracy(userId));
        return statsDTO;
    }


    // =============== HELPERS ===============

    private void checkIfHasAllItems(AvatarDTO avatarDTO, User user) {
        ShopItem hair = shopService.getItem(avatarDTO.getHair().getId());
        ShopItem shirt = shopService.getItem(avatarDTO.getShirt().getId());
        ShopItem skin = shopService.getItem(avatarDTO.getSkin().getId());
        ShopItem background = shopService.getItem(avatarDTO.getBackground().getId());
        List<ShopItem> itemsToCheck = List.of(hair,shirt,skin,background);
        for (ShopItem item : itemsToCheck) {
            checkIfHasItem(item, user);
        }
    }

    private void checkIfHasItem(ShopItem item, User user) {
        if (!user.getItems().contains(item) && item.getPrice() != 0) {
            throw new SomeItemsWereNotBought("Items " + item.getId() + " were not bought");
        }
    }

    private void checkAndUpdateUserLevel(User user) {
        Level appropriateLevel = levelRepository
                .findFirstByMinPointsLessThanEqualOrderByMinPointsDesc(user.getPoints())
                .orElse(user.getLevel());

        if (!appropriateLevel.getId().equals(user.getLevel().getId())) {
            user.setLevel(appropriateLevel);
        }
    }

}
