package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribers, Long> {
}
