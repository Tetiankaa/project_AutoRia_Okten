package autoria.service;

import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(String recipient, String message, String subject ){

        SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setTo(recipient);
            mailMessage.setFrom(sender);

        mailSender.send(mailMessage);
    }
}
