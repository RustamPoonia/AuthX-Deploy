package com.authx.services.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail,String name)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welome to Authx");
        message.setText("Hello"+name+",\n\nThanks for registering with us!\n\nRegards,\nAuthX Team");

        mailSender.send(message);
    }

    public void sendResetOtpEmail(String toEmail,String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password reset otp");
        message.setText("your otp for resetting your password is "+otp+", use this otp to procees with reseating your password");

        mailSender.send(message);
    }

    public void sendVerificationOtpEmail(String toEmail,String otp){
        System.out.println("Entered into the verify otp try block");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Account verification otp");
        message.setText("your otp for account verification is "+otp+", use this otp to verify your account");

        mailSender.send(message);
    }
}
