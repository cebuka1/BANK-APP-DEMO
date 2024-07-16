package com.bankingAppDemo.bankingAppDemo.service.Impl;

import com.bankingAppDemo.bankingAppDemo.payload.request.EmailDetails;
import com.bankingAppDemo.bankingAppDemo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

//    private final JavaMailSender javaMailSender;
private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;
    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();


            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

}
