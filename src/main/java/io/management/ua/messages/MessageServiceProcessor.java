package io.management.ua.messages;


import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.exceptions.ActionRestrictedException;
import io.management.ua.mails.service.MailService;
import io.management.ua.telegram.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceProcessor {
    private final MailService mailService;
    private final TelegramService telegramService;

    public void processMessage(MessageModel messageModel) {
        switch (messageModel.getMessagePlatform()) {
            case TELEGRAM -> telegramService.processMessage(messageModel);
            case EMAIL -> mailService.processMessage(messageModel);
            default -> throw new ActionRestrictedException("Unsupported message platform");
        }
    }

    public List<MessageModel> getMessageHistory(ZonedDateTime start, ZonedDateTime end) {
        List<MessageModel> messages = mailService.getMessageHistory(start, end);
        messages.addAll(telegramService.getMessageHistory(start, end));

        return messages;
    }
}
