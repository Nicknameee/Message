package io.management.ua.messages;

import io.management.ua.amqp.models.messages.MessageModel;

import java.time.ZonedDateTime;
import java.util.List;

public interface MessageService {
    void processMessage(MessageModel messageModel);

    void clearMessageCache();

    List<MessageModel> getMessageHistory(ZonedDateTime start, ZonedDateTime end);
}
