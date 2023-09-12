package com.github.infinitumus.wizard_bot.bot_api;

import com.github.infinitumus.wizard_bot.MyWizardBot;
import com.github.infinitumus.wizard_bot.model.UserProfileData;
import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import com.github.infinitumus.wizard_bot.service.MainMenuService;
import com.github.infinitumus.wizard_bot.service.ReplyMessageService;
import com.github.infinitumus.wizard_bot.service.UserProfileDataService;
import com.github.infinitumus.wizard_bot.utils.Emojis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;

@Component
@Slf4j
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;

    private final UserProfileDataService userProfileDataService;
    private final MainMenuService mainMenuService;
    private final MyWizardBot myWizardBot;
    private final ReplyMessageService messageService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, UserProfileDataService userProfileDataService, MainMenuService mainMenuService, @Lazy MyWizardBot myWizardBot, ReplyMessageService messageService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.userProfileDataService = userProfileDataService;
        this.mainMenuService = mainMenuService;
        this.myWizardBot = myWizardBot;
        this.messageService = messageService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            log.info("New message from User:{}, userId: {},  with text: {}", update.getCallbackQuery().getFrom().getUserName(),
                    update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        long userId = message.getFrom().getId();
        String chatId = String.valueOf(message.getChatId());
        BotState botState = userDataCache.getCurrentBotState(userId);
        SendMessage replyMessage;
        switch (inputMessage) {
            case "/start" -> {
                botState = BotState.ASK_FATE;
                myWizardBot.sendPhoto(chatId, messageService.getReplyText("reply.hello"), "static/images/gandalf.jpg");
            }
            case "Получить предсказание" -> botState = BotState.FILLING_PROFILE;
            case "Анкета" -> botState = BotState.SHOW_USER_PROFILE;
            case "Скачать анкету" -> {
                myWizardBot.sendDocument(chatId, "Ваша анкета", getUserProfile(userId));
                botState = BotState.SHOW_MAIN_MENU;
            }
            case "Помощь" -> botState = BotState.SHOW_HELP_MENU;
            default -> botState = userDataCache.getCurrentBotState(userId);
        }
        userDataCache.setCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }

    private File getUserProfile(long userId) {
        UserProfileData userProfileData = userProfileDataService.getUserProfileData(userId);
        File profile = null;
        if (userProfileData != null) {
            try {
                profile = ResourceUtils.getFile("classpath:static/docs/profile.txt");
                try (FileWriter fileWriter = new FileWriter(profile.getAbsolutePath());
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                    bufferedWriter.write(userProfileData.getProfile());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return profile;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final String chatId = String.valueOf(buttonQuery.getMessage().getChatId());
        final long userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callbackAnswer = mainMenuService.getMainMenuMessage(chatId,
                messageService.getReplyText("reply.showMainMenu", Emojis.MAGE));
        //Fate choose button
        if (buttonQuery.getData().equals("buttonYes")) {
            callbackAnswer = messageService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setCurrentBotState(userId, BotState.ASK_AGE);
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callbackAnswer = sendAnswerCallbackQuery("Возвращайся когда будешь готов", false, buttonQuery);
            userDataCache.setCurrentBotState(userId, BotState.ASK_FATE);
        } else if (buttonQuery.getData().equals("buttonThink")) {
            callbackAnswer = sendAnswerCallbackQuery("Кнопка не поддерживается", true, buttonQuery);
            userDataCache.setCurrentBotState(userId, BotState.ASK_FATE);
        }
        //Gender choose button
        else if (buttonQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getProfileData(userId);
            userProfileData.setGender("М");
            userDataCache.saveProfileData(userId, userProfileData);
            userDataCache.setCurrentBotState(userId, BotState.ASK_COLOR);
            callbackAnswer = messageService.getReplyMessage(chatId, "reply.askNum");
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getProfileData(userId);
            userProfileData.setGender("Ж");
            userDataCache.saveProfileData(userId, userProfileData);
            userDataCache.setCurrentBotState(userId, BotState.ASK_COLOR);
            callbackAnswer = messageService.getReplyMessage(chatId, "reply.askNum");
        } else {
            userDataCache.setCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }
        return callbackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

}
