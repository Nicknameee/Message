package io.management.ua.mails.mappers;

import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.mails.entity.Mail;
import io.management.ua.telegram.entity.TelegramMessage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {
    Mail messageModelToMail(MessageModel messageModel);
    List<MessageModel> mailsToMessages(List<Mail> mails);
    TelegramMessage messageModelToTelegramMessage(MessageModel messageModel);
    List<MessageModel> telegramMessagesToMessages(List<TelegramMessage> telegramMessages);
}
