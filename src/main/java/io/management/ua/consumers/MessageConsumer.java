package io.management.ua.consumers;

import io.management.ua.amqp.KafkaTopic;
import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.messages.MessageServiceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final MessageServiceProcessor messageServiceProcessor;

    @KafkaListener(topics = KafkaTopic.MESSAGE_TOPIC, groupId = KafkaTopic.MESSAGE_TOPIC)
    public void handle(@Payload MessageModel messageModel) {
        messageServiceProcessor.processMessage(messageModel);
    }
}
