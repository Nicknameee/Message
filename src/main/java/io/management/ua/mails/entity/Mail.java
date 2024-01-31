package io.management.ua.mails.entity;

import io.management.ua.amqp.messages.MessageModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

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
    private ZonedDateTime sendingDate;
    private MessageModel.MessageType messageType;
}

