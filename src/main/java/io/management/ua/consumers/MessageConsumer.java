package io.management.ua.consumers;

import io.management.ua.amqp.KafkaValueParser;
import io.management.ua.amqp.models.KafkaTopic;
import io.management.ua.amqp.models.Message;
import io.management.ua.amqp.models.messages.MessageModel;
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
    public void handle(@Payload Message message) {
        MessageModel messageModel = (MessageModel) KafkaValueParser.parseObject(message);

        messageService.processMessage(messageModel);
    }
}
