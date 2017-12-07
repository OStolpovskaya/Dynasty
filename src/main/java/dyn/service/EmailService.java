package dyn.service;

/**
 * Created by OM on 06.12.2017.
 */

import org.springframework.mail.SimpleMailMessage;


public interface EmailService {
    public void sendEmail(SimpleMailMessage email);
}