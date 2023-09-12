package com.github.infinitumus.wizard_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Пользовательские данные
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "userProfileData")
public class UserProfileData implements Serializable {

    String name;
    String gender;
    String color;
    String film;
    String song;
    int age;
    int num;
    @Id
    long chatId;

    public String getProfile() {
        return String.format("Имя: %s%n" +
                        "Возраст: %d%nПол: %s%nЛюбимая цифра: %d%nЦвет: %s%nФильм: %s%nПесня: %s%n",
                name, age, gender, num, color, film, song);
    }

    @Override
    public String toString() {
        return String.format("%s%n----------------------%nИмя: %s%n" +
                        "Возраст: %d%nПол: %s%nЛюбимая цифра: %d%nЦвет: %s%nФильм: %s%nПесня: %s%n",
                "Данные вашей анкеты", name, age, gender, num, color, film, song);
    }
}
