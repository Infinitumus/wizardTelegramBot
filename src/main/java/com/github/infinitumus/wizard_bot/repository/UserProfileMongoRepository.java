package com.github.infinitumus.wizard_bot.repository;

import com.github.infinitumus.wizard_bot.model.UserProfileData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileMongoRepository extends MongoRepository<UserProfileData, String> {
    UserProfileData findByChatId(long chatId);
    void deleteByChatId(long chatId);
}
