package com.github.infinitumus.wizard_bot.appconfig;

import com.github.infinitumus.wizard_bot.MyWizardBot;
import com.github.infinitumus.wizard_bot.bot_api.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String userName;
    private String botToken;

    @Bean
    public MyWizardBot telegramBot(TelegramFacade telegramFacade) {
        MyWizardBot wizardBot = new MyWizardBot(telegramFacade);
        wizardBot.setBotToken(botToken);
        wizardBot.setUserName(userName);
        wizardBot.setWebHookPath(webHookPath);

        return wizardBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
