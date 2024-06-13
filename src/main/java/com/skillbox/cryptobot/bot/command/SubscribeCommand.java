package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscribers;
import com.skillbox.cryptobot.repository.SubscribesRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {
    private final SubscribesRepository subscribesRepository;
    private final CryptoCurrencyService cryptoCurrencyService;

    public SubscribeCommand(SubscribesRepository subscribesRepository, CryptoCurrencyService cryptoCurrencyService) {
        this.subscribesRepository = subscribesRepository;
        this.cryptoCurrencyService = cryptoCurrencyService;
    }

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        double price = Integer.parseInt(message.getText().replaceAll("\\D+", ""));
        log.info("Subscribe the user to the price {} ", price);
        addPriceToDatabase(price, message);
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        try {
            String currentBitcoinPrice = TextUtil.toString(getCurrentPriceOfBitcoin());
            answer.setText("Текущая цена биткоина " + currentBitcoinPrice + " USD");
            absSender.execute(answer);
            answer.setText("Новая подписка создана на стоимость " + price);
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /subscribe методе", e);
        }
    }

    public void addPriceToDatabase(double price, Message message) {
        Subscribers subscribers = subscribesRepository.findByChatId(message.getChatId());
        subscribers.setPrice(price);
        subscribesRepository.save(subscribers);
    }

    public double getCurrentPriceOfBitcoin() throws IOException {
        return cryptoCurrencyService.getBitcoinPrice();
    }
}