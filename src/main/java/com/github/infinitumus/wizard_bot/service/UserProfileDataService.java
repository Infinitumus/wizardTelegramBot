package com.github.infinitumus.wizard_bot.service;

import com.github.infinitumus.wizard_bot.model.UserProfileData;
import com.github.infinitumus.wizard_bot.repository.UserProfileMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сохранение, удаление, поиск анкет пользователей
 */
@Service
public class UserProfileDataService {
    private final UserProfileMongoRepository profileMongoRepository;


    public UserProfileDataService(UserProfileMongoRepository profileMongoRepository) {
        this.profileMongoRepository = profileMongoRepository;
    }

    public List<UserProfileData> getAllProfiles(){
        return profileMongoRepository.findAll();
    }
    public void saveUser(UserProfileData userProfileData){
        profileMongoRepository.save(userProfileData);
    }
    public void deleteUserProfile(String userProfileDataId){
        profileMongoRepository.deleteById(userProfileDataId);
    }
    public UserProfileData getUserProfileData(long chatId){
        return profileMongoRepository.findByChatId(chatId);
    }
}
