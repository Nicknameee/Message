package io.management.ua.controllers;

import io.management.ua.messages.MessageServiceProcessor;
import io.management.ua.response.Response;
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

    @GetMapping("/history")
    @PreAuthorize("!hasRole(T(io.management.ua.utility.models.UserSecurityRole).ROLE_CUSTOMER)")
    public Response<?> getMessageHistory(@RequestParam("start") @DateTimeFormat(pattern = "dd.MM.yyyy") Date start,
                                         @RequestParam("end") @DateTimeFormat(pattern = "dd.MM.yyyy") Date end) {
        return Response.ok(messageServiceProcessor.getMessageHistory(start.toInstant().atZone(ZoneId.of("UTC")), end.toInstant().atZone(ZoneId.of("UTC"))));
    }
}
