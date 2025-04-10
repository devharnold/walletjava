package com.fintech.fintechapp.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendReceipt(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        
    }
}