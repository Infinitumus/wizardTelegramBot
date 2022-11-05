package com.github.infinitumus.wizard_bot.cache;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.model.UserProfileData;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
@Component
public class UserDataCache implements DataCache{
    private final Map<Long, BotState> usersBotState = new HashMap<>();
    private final Map<Long, UserProfileData> usersProfileData = new HashMap<>();

    @Override
    public void setCurrentBotState(long userId, BotState botState) {
        usersBotState.put(userId, botState);
    }

    @Override
    public BotState getCurrentBotState(long userId) {
        BotState botState = usersBotState.get(userId);
        if (botState == null){
            botState = BotState.ASK_FATE;
        }
        return botState;
    }

    @Override
    public void saveProfileData(long userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }

    @Override
    public UserProfileData getProfileData(long userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        if (userProfileData ==null){
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }
}
