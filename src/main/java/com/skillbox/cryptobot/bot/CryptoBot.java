package com.skillbox.cryptobot.bot;

import com.skillbox.cryptobot.dto.SubscribersDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class CryptoBot extends TelegramLongPollingCommandBot {

    private final String botUsername;

    public CryptoBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            List<IBotCommand> commandList
    ) {
        super(botToken);
        this.botUsername = botUsername;
        commandList.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
    }

    public void sendMessage(List<SubscribersDto> subscriberDto, String currentPrice) {
        for (SubscribersDto user : subscriberDto) {
            if (user.getLastNotificationTime() == null ||
                    user.getLastNotificationTime().plusMinutes(10).isBefore(LocalDateTime.now())) {
                SendMessage answer = new SendMessage();
                answer.setChatId(user.getChatId());
                answer.setText("Пора покупать, стоимость биткоина " + currentPrice);
                user.setLastNotificationTime(LocalDateTime.now());
                try {
                    execute(answer);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
