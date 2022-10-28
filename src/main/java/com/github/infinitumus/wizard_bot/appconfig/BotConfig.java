package com.github.infinitumus.wizard_bot.appconfig;

import com.github.infinitumus.wizard_bot.MyWizardBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

@Bean
    public MyWizardBot telegramBot(){
        MyWizardBot wizardBot = new MyWizardBot();
        wizardBot.setBotToken(botToken);
        wizardBot.setBotUserName(botUserName);
        wizardBot.setWebHookPath(webHookPath);
        return wizardBot;
    }
}
