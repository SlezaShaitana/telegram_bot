package com.skillbox.cryptobot.dto;

import com.skillbox.cryptobot.entity.Subscribers;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SubscribersDto {
    private Long id;
    private UUID UUID;
    private Long chatId;
    private Double price;
    private LocalDateTime lastNotificationTime;

    public SubscribersDto(Long id, java.util.UUID UUID, Long chatId, Double price) {
        this.id = id;
        this.UUID = UUID;
        this.chatId = chatId;
        this.price = price;
    }

    public static SubscribersDto fromSubscribers(Subscribers subscribers) {
        return new SubscribersDto(subscribers.getId(), subscribers.getUUID(), subscribers.getChatId(), subscribers.getPrice());
    }
}
