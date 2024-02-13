package io.management.ua;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.management")
@Slf4j
public class Message {
    public static void main(String[] args) {
        log.debug("Message service started");
        SpringApplication.run(Message.class);
    }
}
