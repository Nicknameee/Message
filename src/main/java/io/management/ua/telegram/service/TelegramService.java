package io.management.ua.telegram.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.mails.mappers.MessageMapper;
import io.management.ua.telegram.entity.TelegramMessage;
import io.management.ua.telegram.entity.TelegramSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TelegramService {
    private TelegramSubscriberService telegramSubscriberService;
    private TelegramMessageService telegramMessageService;
    private MessageMapper messageMapper;
    private AbstractEnvironment abstractEnvironment;

    private final TelegramBot telegramBot;

    public TelegramService(AbstractEnvironment abstractEnvironment) {
        this.telegramBot = new TelegramBot(abstractEnvironment.getProperty("telegram.bot.token"));
    }
    @Autowired
    public void setTelegramSubscriberService(TelegramSubscriberService telegramSubscriberService) {
        this.telegramSubscriberService = telegramSubscriberService;
    }

    @Autowired
    public void setTelegramMessageService(TelegramMessageService telegramMessageService) {
        this.telegramMessageService = telegramMessageService;
    }
    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setAbstractEnvironment(AbstractEnvironment abstractEnvironment) {
        this.abstractEnvironment = abstractEnvironment;
    }

    @PostConstruct
    public void initListener() {
        telegramBot.setUpdatesListener(updates -> {
            try {
                for (Update update : updates) {
                    if (update.myChatMember() != null) {
                        if (update.myChatMember().newChatMember().status() == ChatMember.Status.member) {
                            telegramSubscriberService.save(new TelegramSubscriber(update.myChatMember().from().username(), update.myChatMember().chat().id()));
                        } else {
                            telegramSubscriberService.deleteByUsername(update.myChatMember().from().username());
                        }
                    } else {
                        if (update.message() != null) {
                            telegramSubscriberService.save(new TelegramSubscriber(update.message().from().username(), update.message().chat().id()));
                            switch (update.message().text()) {
                                case "/start" -> {
                                    SendMessage startMessage = new SendMessage(update.message().chat().id(), "Welcome, you started using CRM Assistant Bot, now you can proceed your actions gratefully!");
                                    telegramBot.execute(startMessage);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                log.error("Error occurred with code {} and exception {}",
                        e.response().errorCode(), e.response().description());
        }});
    }

    public void processMessage(MessageModel messageModel) {
        TelegramSubscriber telegramSubscriber = telegramSubscriberService.getByUsername(messageModel.getReceiver());
        SendMessage sendMessage = new SendMessage(telegramSubscriber.getChatId(), messageModel.getContent());
        telegramBot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                TelegramMessage telegramMessage = messageMapper.messageModelToTelegramMessage(messageModel);
                telegramMessage.setSender(abstractEnvironment.getProperty("telegram.bot.username"));

                telegramMessageService.save(telegramMessage);
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                log.error(e.getMessage());
            }
        });
    }

    public List<MessageModel> getMessageHistory(ZonedDateTime start, ZonedDateTime end) {
        List<TelegramMessage> telegramMessages = telegramMessageService.findAll().stream()
                .filter(mail -> !mail.getSendingDate().isBefore(start) && !mail.getSendingDate().isAfter(end))
                .toList();

        return messageMapper.telegramMessagesToMessages(telegramMessages);
    }

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.DAYS)
    public void clearMessageCache() {
        telegramMessageService.deleteAll();
    }
}
