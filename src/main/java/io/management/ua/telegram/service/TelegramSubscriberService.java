package io.management.ua.telegram.service;

import io.management.ua.exceptions.NotFoundException;
import io.management.ua.telegram.entity.TelegramSubscriber;
import io.management.ua.telegram.repository.TelegramSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramSubscriberService {
    private final TelegramSubscriberRepository telegramSubscriberRepository;

    public void save(TelegramSubscriber telegramSubscriber) {
        telegramSubscriberRepository.save(telegramSubscriber);
    }

    public boolean existsByUsername(String username) {
        return telegramSubscriberRepository.existsById(username);
    }
    public TelegramSubscriber getByUsername(String username) {
        return telegramSubscriberRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Telegram subscriber was not found"));
    }

    public void deleteByUsername(String username) {
        telegramSubscriberRepository.deleteById(username);
    }
}
