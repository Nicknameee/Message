package io.management.ua.telegram.entity;

import io.management.ua.amqp.messages.MessageModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@RedisHash("telegramMessage")
public class TelegramMessage {
    @Id
    private UUID id = UUID.randomUUID();
    private String receiver;
    private String sender;
    private String content;
    private String subject;
    private ZonedDateTime sendingDate;
    private ZonedDateTime expiringDate;
    private MessageModel.MessageType messageType;
    private MessageModel.MessagePlatform messagePlatform;
}
