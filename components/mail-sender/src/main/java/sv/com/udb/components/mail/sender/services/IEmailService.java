package sv.com.udb.components.mail.sender.services;

import sv.com.udb.components.mail.sender.model.Mail;

import javax.mail.MessagingException;

public interface IEmailService {
   void sendEmail(Mail mail) throws MessagingException;
}
