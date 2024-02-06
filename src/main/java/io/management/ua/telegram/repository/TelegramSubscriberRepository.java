package io.management.ua.telegram.repository;

import io.management.ua.telegram.entity.TelegramSubscriber;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.Optional;

public interface TelegramSubscriberRepository extends KeyValueRepository<TelegramSubscriber, String> {
    Optional<TelegramSubscriber> findByUsername(String username);
}
