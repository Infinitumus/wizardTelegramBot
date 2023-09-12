package com.github.infinitumus.wizard_bot;

import com.github.infinitumus.wizard_bot.bot_api.TelegramFacade;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;

public class MyWizardBot extends TelegramWebhookBot {

    private String webHookPath;
    private String userName;
    private String botToken;
    private final TelegramFacade telegramFacade;

    public MyWizardBot(TelegramFacade telegramFacade) {
        this.telegramFacade = telegramFacade;
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void sendPhoto(String chatId, String imageCaption, String imagePath) {
        try {
            InputFile image = new InputFile(ResourceUtils.getFile("classpath:" + imagePath));
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(image);
            sendPhoto.setChatId(chatId);
            sendPhoto.setCaption(imageCaption);
            execute(sendPhoto);
        } catch (TelegramApiException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDocument(String chatId, String caption, File sendFile){
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(caption);
        sendDocument.setDocument(new InputFile(sendFile));
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
