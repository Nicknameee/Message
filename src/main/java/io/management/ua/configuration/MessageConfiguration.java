package io.management.ua.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class MessageConfiguration {
    private final MessageConfigurationProperties messageConfigurationProperties;
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setDefaultEncoding(messageConfigurationProperties.getDefaultEncoding());
        javaMailSender.setHost(messageConfigurationProperties.getHost());
        javaMailSender.setUsername(messageConfigurationProperties.getUsername());
        javaMailSender.setPassword(messageConfigurationProperties.getPassword());
        javaMailSender.setPort(messageConfigurationProperties.getPort());
        javaMailSender.setProtocol(messageConfigurationProperties.getProtocol());

        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", String.valueOf(messageConfigurationProperties.isStartTlsEnable()));
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.required", String.valueOf(messageConfigurationProperties.isStartTlsRequired()));
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", String.valueOf(messageConfigurationProperties.isSmtpAuth()));
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.ssl.trust", messageConfigurationProperties.getSslTrust());

        return javaMailSender;
    }
}
