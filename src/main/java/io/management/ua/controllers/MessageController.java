package io.management.ua.controllers;

import io.management.ua.messages.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;


    @GetMapping("/history")
    @PreAuthorize(value = "hasAuthority(T(io.management.ua.utility.security.models.UserSecurityPermissions).managerReadPermission)")
    public ResponseEntity<?> getMessageHistory(@RequestParam("start") @DateTimeFormat(pattern = "dd.MM.yyyy")ZonedDateTime start,
                                               @RequestParam("end") @DateTimeFormat(pattern = "dd.MM.yyyy")ZonedDateTime end) {
        return ResponseEntity.ok(messageService.getMessageHistory(start, end));
    }
}
