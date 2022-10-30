package com.github.infinitumus.wizard_bot.bot_api.handlers.ask_fate;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.InputMessageHandler;
import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import com.github.infinitumus.wizard_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 *Спрашивает нужно ли предсказание
 */
@Slf4j
@Component
public class AskFateHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public AskFateHandler(UserDataCache userDataCache, ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
    }


    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_FATE;
    }

    private SendMessage processUsersInput(Message inputMessage){
        long userId = inputMessage.getFrom().getId();
        String chatId = String.valueOf(inputMessage.getChatId());

        SendMessage replyMessage = messageService.getReplyMessage(chatId, "reply.askFate");
        userDataCache.setCurrentBotState(userId, BotState.FILLING_PROFILE);
        return replyMessage;
    }
}
