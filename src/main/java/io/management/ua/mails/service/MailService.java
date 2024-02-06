package io.management.ua.mails.service;

import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.exceptions.ActionRestrictedException;
import io.management.ua.mails.entity.Mail;
import io.management.ua.mails.mappers.MessageMapper;
import io.management.ua.mails.repository.MailRepository;
import io.management.ua.messages.MessageService;
import io.management.ua.utility.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MailService implements MessageService {
    private final MailRepository mailRepository;
    private final JavaMailSender javaMailSender;
    private final MessageMapper messageMapper;
    private final Environment environment;

    @Override
    public void processMessage(MessageModel messageModel) {
        Mail mail = messageMapper.messageModelToMail(messageModel);
        mail.setSender(environment.getProperty("spring.mail.username"));

        switch (messageModel.getMessageType()) {
            case PLAIN_TEXT ->
                    javaMailSender.send(getMailMessage(mail));
            case HTML -> {
                try {
                    javaMailSender.send(getHtmlMailMessage(mail));
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
            case WITH_FILE -> throw new ActionRestrictedException("Media files currently not supported");
            default -> throw new ActionRestrictedException("Unknown message type or type was not specified");
        }

        mailRepository.save(mail);
    }

    private SimpleMailMessage getMailMessage(Mail mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSentDate(TimeUtil.getCurrentDate());
        simpleMailMessage.setFrom(mail.getSender());
        simpleMailMessage.setTo(mail.getReceiver());
        simpleMailMessage.setText(mail.getContent());
        simpleMailMessage.setSubject(mail.getSubject());

        return simpleMailMessage;
    }

    private MimeMessage getHtmlMailMessage(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setFrom(mail.getSender());
        helper.setTo(mail.getReceiver());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent(), true);

        return mimeMessage;
    }

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.DAYS)
    public void clearMessageCache() {
        mailRepository.deleteAll();
    }

    public List<MessageModel> getMessageHistory(ZonedDateTime start, ZonedDateTime end) {
        List<Mail> mails = StreamSupport.stream(mailRepository.findAll().spliterator(), false)
                .filter(mail -> !mail.getSendingDate().isBefore(start) && !mail.getSendingDate().isAfter(end))
                .toList();

        return messageMapper.mailsToMessages(mails);
    }
}
