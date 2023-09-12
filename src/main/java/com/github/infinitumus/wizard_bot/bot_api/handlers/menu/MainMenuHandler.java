package com.github.infinitumus.wizard_bot.bot_api.handlers.menu;

import com.github.infinitumus.wizard_bot.bot_api.BotState;
import com.github.infinitumus.wizard_bot.bot_api.InputMessageHandler;
import com.github.infinitumus.wizard_bot.service.MainMenuService;
import com.github.infinitumus.wizard_bot.service.ReplyMessageService;
import com.github.infinitumus.wizard_bot.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MainMenuHandler implements InputMessageHandler {
    private final ReplyMessageService messageService;
    private final MainMenuService mainMenuService;

    public MainMenuHandler(ReplyMessageService messageService, MainMenuService mainMenuService) {
        this.messageService = messageService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        String chatId = String.valueOf(message.getChatId());
        return mainMenuService.getMainMenuMessage(chatId,
                messageService.getReplyText("reply.showMainMenu", Emojis.MAGE));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
