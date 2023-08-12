package io.management.ua.mails.mappers;

import io.management.ua.amqp.models.messages.MessageModel;
import io.management.ua.mails.entity.Mail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MailMapper {
    Mail messageModelToMail(MessageModel messageModel);
    List<MessageModel> mailsToMessages(List<Mail> mails);
}
