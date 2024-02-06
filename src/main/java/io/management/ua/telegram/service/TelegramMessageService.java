package io.management.ua.telegram.service;

import io.management.ua.telegram.entity.TelegramMessage;
import io.management.ua.telegram.repository.TelegramMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramMessageService {
    private final TelegramMessageRepository telegramMessageRepository;

    public void save(TelegramMessage telegramMessage) {
        telegramMessageRepository.save(telegramMessage);
    }

    public List<TelegramMessage> findAll() {
        return StreamSupport.stream(telegramMessageRepository.findAll().spliterator(), false).toList();
    }
}
