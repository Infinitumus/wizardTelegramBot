package com.github.infinitumus.wizard_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**Работает с файлом "ответов" message.properties
 *
 */

@Service
public class LocaleMessageService {
    private final Locale locale;
    private final MessageSource messageSource;

    public LocaleMessageService(@Value("${localeTag}") String localeTag, MessageSource messageSource) {
        this.locale = Locale.forLanguageTag(localeTag);
        this.messageSource = messageSource;
    }

    public String getMessage(String message){
        return messageSource.getMessage(message, null, locale);
    }

    public String getMessage(String message, Object... args){
        return messageSource.getMessage(message, args, locale);
    }
}
