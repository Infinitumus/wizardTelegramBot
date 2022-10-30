package com.github.infinitumus.wizard_bot.bot_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * определяет обработчик сообщений для каждого состояния
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(@Autowired List<InputMessageHandler> messageHandlers){
        messageHandlers.forEach(handler ->this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message){
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }
    private InputMessageHandler findMessageHandler(BotState currentState){
        if (isFillingProfileState(currentState)){
            return messageHandlers.get(BotState.FILLING_PROFILE);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotState currentState) {
        return switch (currentState) {
            case ASK_AGE, ASK_NUM, PROFILE_FILLED, ASK_FILM, ASK_NAME, FILLING_PROFILE, ASK_SONG, ASK_COLOR, ASK_GENDER ->
                    true;
            default -> false;
        };
    }

}
