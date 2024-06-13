package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscribers;
import com.skillbox.cryptobot.repository.SubscribesRepository;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {
    private final SubscribesRepository subscribesRepository;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        Subscribers user = subscribesRepository.findByChatId(message.getChatId());
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            if (user.getPrice() != null) {
                String subscribedToPrice = TextUtil.toString(user.getPrice());
                answer.setText("Вы подписаны на стоимость биткоина " + subscribedToPrice);
            } else {
                answer.setText("Активные подписки отсутствуют");
            }
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_subscription методе", e);
        }
    }
}