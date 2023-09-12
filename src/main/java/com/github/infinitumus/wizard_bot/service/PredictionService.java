package com.github.infinitumus.wizard_bot.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Генератор предсказаний
 */
@Service
public class PredictionService {
    private final Random random = new Random();
    private final ReplyMessageService messageService;

    public PredictionService(ReplyMessageService messageService) {
        this.messageService = messageService;
    }

    public String getPrediction(){
        int predictionNum = random.nextInt(5);
        String replyMessageProp = String.format("%s%d", "reply.prediction", predictionNum);
        return messageService.getReplyText(replyMessageProp);
    }
}
