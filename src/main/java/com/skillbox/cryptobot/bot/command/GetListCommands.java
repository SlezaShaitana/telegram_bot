package com.skillbox.cryptobot.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Service
public class GetListCommands implements IBotCommand {
    @Override
    public String getCommandIdentifier() {
        return "list_commands";
    }

    @Override
    public String getDescription() {
        return "Возвращает список команд";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            answer.setText("Доступны команды : " +
                    '\n' + "/get_price" + " - получить текущую стоимость биткоина" +
                    '\n' + "/get_subscription" + " - получить текущую подписку" +
                    '\n' + "/start" +
                    '\n' + "/subscribe" + "[число] - подписаться на стоимость биткоина в USD" +
                    '\n' + "/unsubscribe" + " - отменить подписку на стоимость");
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_price методе", e);
        }
    }
}
