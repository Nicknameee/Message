package io.management.ua.consumers;

import io.management.ua.amqp.KafkaTopic;
import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.messages.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final MessageService messageService;

    @KafkaListener(topics = KafkaTopic.MESSAGE_TOPIC, groupId = KafkaTopic.MESSAGE_TOPIC)
    public void handle(@Payload MessageModel messageModel) {
        messageService.processMessage(messageModel);
    }
}
