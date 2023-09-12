package com.github.infinitumus.wizard_bot.bot_api.handlers.menu;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.InputMessageHandler;
import com.github.infinitumus.wizard_bot.model.UserProfileData;
import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import com.github.infinitumus.wizard_bot.service.UserProfileDataService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowProfileHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final UserProfileDataService profileDataService;

    public ShowProfileHandler(UserDataCache userDataCache, UserProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.profileDataService = profileDataService;
    }
    @Override
    public SendMessage handle(Message message) {
        SendMessage replyMessage;
        String chatId = String.valueOf(message.getChatId());
        final long userId = message.getFrom().getId();
        //   final UserProfileData profileData = userDataCache.getProfileData(userId);
        //Теперь берем данные не из кэша, а из репозитория БД
        final UserProfileData profileData = profileDataService.getUserProfileData(Long.parseLong(chatId));
        userDataCache.setCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

        if (profileData != null){
            replyMessage = new SendMessage(chatId, profileData.toString());
        }else {
            replyMessage = new SendMessage(chatId, "Вашей анкеты нет в базе");
        }
        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
