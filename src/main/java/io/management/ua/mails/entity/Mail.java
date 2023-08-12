package io.management.ua.mails.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@RedisHash("mail")
public class Mail implements Serializable {
    @Id
    private String receiver;
    private String sender;
    private String content;
    private String subject;
    private ZonedDateTime sendingDate;
}

