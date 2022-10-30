package com.github.infinitumus.wizard_bot.bot_api;

import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public SendMessage handleUpdate(Update update){
        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if (message != null && message.hasText()){
            log.info("New message from User: text: {}",
                    message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }
    private SendMessage handleInputMessage(Message message){
        String inputMessage = message.getText();
        long userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;
        botState = switch (inputMessage) {
            case "/start" -> BotState.ASK_FATE;
            case "Получить предсказание" -> BotState.FILLING_PROFILE;
            case "Помощь" -> BotState.SHOW_HELP_MENU;
            default -> userDataCache.getCurrentBotState(userId);
        };
        userDataCache.setCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }
}
