package io.management.ua.telegram.repository;

import io.management.ua.telegram.entity.TelegramMessage;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.UUID;

public interface TelegramMessageRepository extends KeyValueRepository<TelegramMessage, UUID> {
}
