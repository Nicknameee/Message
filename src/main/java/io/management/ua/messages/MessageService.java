package io.management.ua.messages;


import io.management.ua.amqp.messages.MessageModel;

public interface MessageService {
    void processMessage(MessageModel messageModel);
}
