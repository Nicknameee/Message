package io.management.ua.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MessageConfiguration {
    @Value("${spring.mail.default-encoding}")
    private String defaultEncoding;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean startTlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean startTlsRequired;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.ssl.trust}")
    private String sslTrust;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setDefaultEncoding(defaultEncoding);
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setProtocol(protocol);

        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", String.valueOf(startTlsEnable));
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.required", String.valueOf(startTlsRequired));
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", String.valueOf(smtpAuth));
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.ssl.trust", sslTrust);

        return javaMailSender;
    }
}
