package com.github.infinitumus.wizard_bot.bot_api.handlers.filling_profile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Пользовательские данные
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {
    String name;
    String gender;
    String color;
    String film;
    String song;
    int age;
    int num;
}
