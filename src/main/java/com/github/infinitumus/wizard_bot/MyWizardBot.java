package com.github.infinitumus.wizard_bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyWizardBot extends TelegramWebhookBot {

    private String webHookPath;
    private String botUserName;
    private String botToken;

    public MyWizardBot() {
    }

    public MyWizardBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()){
            long chatId = update.getMessage().getChatId();

            try {
                execute(new SendMessage(String.valueOf(chatId), "привет"));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
