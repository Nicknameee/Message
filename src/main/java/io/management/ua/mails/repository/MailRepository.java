package io.management.ua.mails.repository;

import io.management.ua.mails.entity.Mail;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface MailRepository extends CrudRepository<Mail, Long> {
    List<Mail> getMailBySendingDateBetween(ZonedDateTime start, ZonedDateTime end);
}
