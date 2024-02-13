package io.management.ua.telegram.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.mails.mappers.MessageMapper;
import io.management.ua.telegram.entity.TelegramMessage;
import io.management.ua.telegram.entity.TelegramSubscriber;
import io.management.ua.utility.enums.WebSocketTopics;
import io.management.ua.utility.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private WebSocketService webSocketService;

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

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @PostConstruct
    @Transactional
    public void initListener() {
        telegramBot.setUpdatesListener(updates -> {
            try {
                for (Update update : updates) {
                    if (update.myChatMember() != null) {
                        if (update.myChatMember().newChatMember().status() == ChatMember.Status.member) {
                            telegramSubscriberService.save(new TelegramSubscriber(update.myChatMember().from().username(), update.myChatMember().chat().id()));
                            webSocketService.sendMessage(WebSocketTopics.TELEGRAM_SUBSCRIPTION.getTopic() + "/" + update.myChatMember().from().username(), true);
                        } else {
                            telegramSubscriberService.deleteByUsername(update.myChatMember().from().username());
                            webSocketService.sendMessage(WebSocketTopics.TELEGRAM_SUBSCRIPTION.getTopic() + "/" +  update.myChatMember().from().username(), false);
                        }
                    } else {
                        if (update.message() != null) {
                            if (!telegramSubscriberService.existsByUsername(update.message().from().username())) {
                                telegramSubscriberService.save(new TelegramSubscriber(update.message().from().username(), update.message().chat().id()));
                                webSocketService.sendMessage(WebSocketTopics.TELEGRAM_SUBSCRIPTION.getTopic() + "/" + update.message().from().username(), true);
                            }
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
        try {
            TelegramSubscriber telegramSubscriber = telegramSubscriberService.getByUsername(messageModel.getReceiver());

            SendMessage sendMessage = new SendMessage(telegramSubscriber.getChatId(), messageModel.getContent());
            sendMessage = sendMessage.parseMode(ParseMode.MarkdownV2);
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
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
