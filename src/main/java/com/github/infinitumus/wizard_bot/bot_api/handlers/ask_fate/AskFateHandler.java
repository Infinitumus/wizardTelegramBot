package com.github.infinitumus.wizard_bot.bot_api.handlers.ask_fate;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.InputMessageHandler;
import com.github.infinitumus.wizard_bot.cache.UserDataCache;
import com.github.infinitumus.wizard_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 *Спрашивает нужно ли предсказание
 */
@Slf4j
@Component
public class AskFateHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;

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
        replyMessage.setReplyMarkup(getInlineMessageButtons());
        userDataCache.setCurrentBotState(userId, BotState.FILLING_PROFILE);
        return replyMessage;
    }
    private InlineKeyboardMarkup getInlineMessageButtons(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        buttonYes.setText("Да");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        buttonNo.setText("Нет");
        InlineKeyboardButton buttonThink = new InlineKeyboardButton();
        buttonThink.setText("Я ещё подумаю");
        InlineKeyboardButton buttonDontKnow = new InlineKeyboardButton();
        buttonDontKnow.setText("Пока не знаю");
    //Чтобы кнопки работали обязательно нужно засетать CallbackData
        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");
        buttonThink.setCallbackData("buttonThink");
        buttonDontKnow.setCallbackData("-");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonThink);
        keyboardButtonsRow2.add(buttonDontKnow);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
