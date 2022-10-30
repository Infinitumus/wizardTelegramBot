package com.github.infinitumus.wizard_bot.cache;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.handlers.filling_profile.UserProfileData;

public interface DataCache {
    void setCurrentBotState(long userId, BotState botState);

    BotState getCurrentBotState(long userId);

    void saveProfileData(long userId, UserProfileData userProfileData);

    UserProfileData getProfileData(long userId);

}
