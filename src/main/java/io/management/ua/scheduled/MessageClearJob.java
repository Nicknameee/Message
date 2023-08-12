package io.management.ua.scheduled;

import io.management.ua.messages.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MessageClearJob {
    private final MessageService messageService;

    @Scheduled(fixedRate = 7, timeUnit = TimeUnit.DAYS)
    public void clearMessageCache() {
        messageService.clearMessageCache();
    }
}
