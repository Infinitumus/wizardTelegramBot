package com.github.infinitumus.wizard_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Формирует готовые сообщения в чат
 */
@Service
public class ReplyMessageService {
    private final LocaleMessageService localeMessageService;

    public ReplyMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getReplyMessage(String chatId, String replyMessage){
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage));
    }
    public SendMessage getReplyMessage(String chatId, String replyMessage, Object... args){
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage, args));
    }

    public String getReplyText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }public String getReplyText(String replyText, Object... args) {
        return localeMessageService.getMessage(replyText, args);
    }
}
