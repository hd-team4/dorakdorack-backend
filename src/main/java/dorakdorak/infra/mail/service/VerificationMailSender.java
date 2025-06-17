package dorakdorak.infra.mail.service;

import jakarta.mail.MessagingException;

public interface VerificationMailSender extends MailSender {
  boolean sendVerificationMail(String sendEmail) throws MessagingException;
}
