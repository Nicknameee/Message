package io.management.ua.telegram.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@AllArgsConstructor
@Data
@RedisHash("telegramSubscriber")
public class TelegramSubscriber implements Serializable {
    @Id
    @Indexed
    private String username;
    private Long chatId;
}

