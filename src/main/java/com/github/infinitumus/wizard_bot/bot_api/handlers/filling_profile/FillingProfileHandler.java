package com.github.infinitumus.wizard_bot.bot_api.handlers.filling_profile;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.InputMessageHandler;
import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import com.github.infinitumus.wizard_bot.model.UserProfileData;
import com.github.infinitumus.wizard_bot.service.PredictionService;
import com.github.infinitumus.wizard_bot.service.ReplyMessageService;
import com.github.infinitumus.wizard_bot.utils.Emojis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Заполнение профиля
 */
@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;
    private final PredictionService predictionService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessageService messageService, PredictionService predictionService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
        this.predictionService = predictionService;
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
            userDataCache.setCurrentBotState(userId, BotState.ASK_AGE);
            replyMessage = messageService.getReplyMessage(chatId, "reply.askName");
        }

        if (botState.equals(BotState.ASK_AGE)) {
            profileData.setName(userAnswer);
            replyMessage = messageService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setCurrentBotState(userId, BotState.ASK_GENDER);
        }

        if (botState.equals(BotState.ASK_GENDER)) {
            try {
                profileData.setAge(Integer.parseInt(userAnswer));
                replyMessage = messageService.getReplyMessage(chatId, "reply.askGender");
                replyMessage.setReplyMarkup(getGenderButtonsMarkup());
            }catch (NumberFormatException e){
                replyMessage = messageService.getReplyMessage(chatId, "reply.askAge2");
                userDataCache.setCurrentBotState(userId, BotState.ASK_GENDER);
            }
        }

        if (botState.equals(BotState.ASK_NUM)) {
            profileData.setGender(userAnswer);
            replyMessage = messageService.getReplyMessage(chatId, "reply.askNum");
            userDataCache.setCurrentBotState(userId, BotState.ASK_COLOR);
        }

        if (botState.equals(BotState.ASK_COLOR)) {
            try {
                profileData.setNum(Integer.parseInt(userAnswer));
                replyMessage = messageService.getReplyMessage(chatId, "reply.askColor");
                userDataCache.setCurrentBotState(userId, BotState.ASK_FILM);
            }catch (NumberFormatException e){
                replyMessage = messageService.getReplyMessage(chatId, "reply.askNum2");
                userDataCache.setCurrentBotState(userId, BotState.ASK_COLOR);
            }
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
            userDataCache.setCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

            String profileFilledMessage = messageService.getReplyText("reply.profileFilled",
                    profileData.getName(), Emojis.SPARKLES);
            String predictionMessage = predictionService.getPrediction();

            replyMessage = new SendMessage(chatId, String.format("%s%n%n%s %s", profileFilledMessage, Emojis.SCROLL, predictionMessage));

            replyMessage.setParseMode("HTML");
        }

        userDataCache.saveProfileData(userId, profileData);
        return replyMessage;
    }

    private InlineKeyboardMarkup getGenderButtonsMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonGenderMan = new InlineKeyboardButton();
        buttonGenderMan.setText("М");
        buttonGenderMan.setCallbackData("buttonMan");
        InlineKeyboardButton buttonGenderWoman = new InlineKeyboardButton();
        buttonGenderWoman.setText("Ж");
        buttonGenderWoman.setCallbackData("buttonWoman");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonGenderMan);
        keyboardButtonsRow1.add(buttonGenderWoman);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
