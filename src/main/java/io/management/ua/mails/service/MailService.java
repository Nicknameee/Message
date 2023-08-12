package io.management.ua.mails.service;

import io.management.ua.amqp.models.messages.MessageModel;
import io.management.ua.mails.entity.Mail;
import io.management.ua.mails.mappers.MailMapper;
import io.management.ua.mails.repository.MailRepository;
import io.management.ua.messages.service.MessageService;
import io.management.ua.utility.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService implements MessageService {
    private final MailRepository mailRepository;
    private final JavaMailSenderImpl javaMailSender;
    private final MailMapper mailMapper;
    @Value("${spring.mail.username}")
    private String source;

    @Override
    public void processMessage(MessageModel messageModel) {
        Mail mail = mailMapper.messageModelToMail(messageModel);
        mail.setSendingDate(TimeUtil.getCurrentDateTime());
        javaMailSender.send(getMailMessage(mail));

        mailRepository.save(mail);
    }

    private SimpleMailMessage getMailMessage(Mail mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSentDate(TimeUtil.getCurrentDate());
        simpleMailMessage.setFrom(source);
        simpleMailMessage.setTo(mail.getReceiver());
        simpleMailMessage.setText(mail.getContent());
        simpleMailMessage.setSubject(mail.getSubject());

        return simpleMailMessage;
    }

    @Override
    public void clearMessageCache() {
        mailRepository.deleteAll();
    }

    @Override
    public List<MessageModel> getMessageHistory(ZonedDateTime start, ZonedDateTime end) {
        List<Mail> mails = mailRepository.getMailBySendingDateBetween(start, end);

        return mailMapper.mailsToMessages(mails);
    }
}
