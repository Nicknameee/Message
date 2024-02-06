package io.management.ua.mails.entity;

import io.management.ua.amqp.messages.MessageModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@RedisHash("mail")
public class Mail implements Serializable {
    @Id
    private UUID id = UUID.randomUUID();
    private String receiver;
    private String sender;
    private String content;
    private String subject;
    @Indexed
    private ZonedDateTime sendingDate;
    private ZonedDateTime expiringDate;
    private MessageModel.MessageType messageType;
    private MessageModel.MessagePlatform messagePlatform;
}

