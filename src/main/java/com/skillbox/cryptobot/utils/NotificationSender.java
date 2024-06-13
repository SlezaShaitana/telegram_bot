package com.skillbox.cryptobot.utils;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.dto.SubscribersDto;
import com.skillbox.cryptobot.entity.Subscribers;
import com.skillbox.cryptobot.repository.SubscribesRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NotificationSender {
    private final CryptoCurrencyService cryptoCurrencyService;
    private final SubscribesRepository subscribesRepository;
    private List<SubscribersDto> subscriberDto;
    private final CryptoBot cryptoBot;

    public NotificationSender(CryptoCurrencyService cryptoCurrencyService, SubscribesRepository subscribesRepository, CryptoBot cryptoBot) {
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.subscribesRepository = subscribesRepository;
        this.cryptoBot = cryptoBot;
        this.subscriberDto = new ArrayList<>();
    }

    @Scheduled(fixedRateString = "${telegram.bot.notify.delay.price-check-interval}")
    public void checkBitcoinPrice() {
        try {
            double price = cryptoCurrencyService.getBitcoinPrice();
            String currentPrice = TextUtil.toString(price);
            currentPrice = currentPrice.replace(",", ".");
            List<Subscribers> users = subscribesRepository.findAllByPriceGreaterThanEqual(Double.parseDouble(currentPrice));
            log.info("Найдено пользователей: " + users.size());
            if (!users.isEmpty() && subscriberDto.isEmpty()) {
                subscriberDto = users.stream()
                        .map(SubscribersDto::fromSubscribers)
                        .collect(Collectors.toList());
            }
            if (users.size() != subscriberDto.size()) {
                users.stream()
                        .filter(user -> !subscriberDto.contains(user.getId()))
                        .forEach(user -> subscriberDto.add(SubscribersDto.fromSubscribers(user)));
            }
            cryptoBot.sendMessage(subscriberDto, currentPrice);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
