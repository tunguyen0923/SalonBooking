package com.salon.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public synchronized void sendEmail(String to, String subject, String body) {
        int maxRetries = 3;
        int retryCount = 0;
        boolean sent = false;

        while (retryCount < maxRetries && !sent) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);

                mailSender.send(message);
                log.info("Email sent successfully to: {}", to);
                sent = true;
                
                // Small delay after success to help avoid hitting rate limit for subsequent calls
                try {
                    Thread.sleep(1010);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                if (e.getMessage() != null && (e.getMessage().contains("Too many emails per second") || e.getMessage().contains("429"))) {
                    log.warn("Rate limit hit for Mailtrap, retrying in 10 seconds... (Attempt {}/{})", retryCount + 1, maxRetries);
                    try {
                        Thread.sleep(10100); // Increased delay
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    retryCount++;
                } else {
                    log.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
                    break; // Non-rate-limit error, don't retry
                }
            }
        }
        
        if (!sent && retryCount >= maxRetries) {
            log.error("Failed to send email to: {} after {} retries due to rate limiting.", to, maxRetries);
        }
    }
}
