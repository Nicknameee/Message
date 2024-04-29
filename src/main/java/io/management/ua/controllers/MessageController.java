package io.management.ua.controllers;

import io.management.ua.amqp.messages.MessageModel;
import io.management.ua.messages.MessageServiceProcessor;
import io.management.ua.response.Response;
import io.management.ua.telegram.service.TelegramSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageServiceProcessor messageServiceProcessor;
    private final TelegramSubscriberService telegramSubscriberService;

    @GetMapping("/history")
    @PreAuthorize("!hasRole(T(io.management.ua.utility.models.UserSecurityRole).ROLE_CUSTOMER)")
    public Response<?> getMessageHistory(@RequestParam("start") @DateTimeFormat(pattern = "dd.MM.yyyy") Date start,
                                         @RequestParam("end") @DateTimeFormat(pattern = "dd.MM.yyyy") Date end,
                                         @RequestParam MessageModel.MessagePlatform messagePlatform,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer sizeOfPage) {
        return Response.ok(messageServiceProcessor.getMessageHistory(start.toInstant().atZone(ZoneId.of("UTC")),
                end.toInstant().atZone(ZoneId.of("UTC")),
                messagePlatform,
                page,
                sizeOfPage));
    }

    @GetMapping("/telegram/exists/allowed")
    public Response<?> customerTelegramRegistered(@RequestParam("username") String telegramUsername) {
        return Response.ok(telegramSubscriberService.existsByUsername(telegramUsername));
    }
}
