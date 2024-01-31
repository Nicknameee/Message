package io.management.ua.mails.repository;

import io.management.ua.mails.entity.Mail;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface MailRepository extends KeyValueRepository<Mail, Long> {
}
