package com.github.infinitumus.wizard_bot.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emojis {
    SPARKLES(EmojiParser.parseToUnicode(":sparkles:")),
    SCROLL(EmojiParser.parseToUnicode(":scroll:")),
    MAGE(EmojiParser.parseToUnicode(":mage:"))
    
    ;
    private final String emojiName;

    Emojis(String emojiName) {
        this.emojiName = emojiName;
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
