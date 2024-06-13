package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribers, Long> {
    Subscribers findByChatId(Long chatId);

    List<Subscribers> findAllByPriceGreaterThanEqual(double price);
}
