package com.github.infinitumus.wizard_bot.bot_api.handlers.menu;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.InputMessageHandler;
import com.github.infinitumus.wizard_bot.model.UserProfileData;
import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowProfileHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;

    public ShowProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }
    @Override
    public SendMessage handle(Message message) {
        String chatId = String.valueOf(message.getChatId());
        final long userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getProfileData(userId);
        userDataCache.setCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        return new SendMessage(chatId, profileData.toString());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
