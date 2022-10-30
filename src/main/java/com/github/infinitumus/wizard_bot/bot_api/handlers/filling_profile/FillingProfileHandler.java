package com.github.infinitumus.wizard_bot.bot_api.handlers.filling_profile;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.InputMessageHandler;
import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import com.github.infinitumus.wizard_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 *Заполнение профиля
 */
@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMessage) {
        String userAnswer = inputMessage.getText();
        long userId = inputMessage.getFrom().getId();
        String chatId = String.valueOf(inputMessage.getChatId());

        UserProfileData profileData = userDataCache.getProfileData(userId);
        BotState botState = userDataCache.getCurrentBotState(userId);
        SendMessage replyMessage = null;
        if (botState.equals(BotState.ASK_NAME)) {
            replyMessage = messageService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setCurrentBotState(userId, BotState.ASK_AGE);
        }

        if (botState.equals(BotState.ASK_AGE)) {
            profileData.setName(userAnswer);
            replyMessage = messageService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setCurrentBotState(userId, BotState.ASK_GENDER);
        }

        if (botState.equals(BotState.ASK_GENDER)) {
            replyMessage = messageService.getReplyMessage(chatId, "reply.askGender");
            profileData.setAge(Integer.parseInt(userAnswer));
            userDataCache.setCurrentBotState(userId, BotState.ASK_NUM);
        }

        if (botState.equals(BotState.ASK_NUM)) {
            replyMessage = messageService.getReplyMessage(chatId, "reply.askNum");
            profileData.setGender(userAnswer);
            userDataCache.setCurrentBotState(userId, BotState.ASK_COLOR);
        }

        if (botState.equals(BotState.ASK_COLOR)) {
            replyMessage = messageService.getReplyMessage(chatId, "reply.askColor");
            profileData.setNum(Integer.parseInt(userAnswer));
            userDataCache.setCurrentBotState(userId, BotState.ASK_FILM);
        }

        if (botState.equals(BotState.ASK_FILM)) {
            replyMessage = messageService.getReplyMessage(chatId, "reply.askFilm");
            profileData.setColor(userAnswer);
            userDataCache.setCurrentBotState(userId, BotState.ASK_SONG);
        }

        if (botState.equals(BotState.ASK_SONG)) {
            replyMessage = messageService.getReplyMessage(chatId, "reply.askSong");
            profileData.setFilm(userAnswer);
            userDataCache.setCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setSong(userAnswer);
            userDataCache.setCurrentBotState(userId, BotState.ASK_FATE);
            replyMessage = new SendMessage(chatId, String.format("%s %s", "Данные по вашей анкете", profileData));
        }

        userDataCache.saveProfileData(userId, profileData);
        return replyMessage;
    }
}
