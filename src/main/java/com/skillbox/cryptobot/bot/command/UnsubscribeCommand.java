package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscribers;
import com.skillbox.cryptobot.repository.SubscribesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {
    private final SubscribesRepository subscribesRepository;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            boolean subscriptionExists = deleteSubscription(message);
            if (subscriptionExists) {
                answer.setText("Подписка отменена");
            } else {
                answer.setText("Активные подписки отсутствуют");
            }
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /unsubscribe методе", e);
        }
    }

    private boolean deleteSubscription(Message message) {
        Subscribers user = subscribesRepository.findByChatId(message.getChatId());
        if (user.getPrice() != null) {
            user.setPrice(null);
            subscribesRepository.save(user);
            return true;
        }
        return false;
    }
}