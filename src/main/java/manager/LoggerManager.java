package manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class LoggerManager {

    // Створюємо логер
    private static final Logger logger = LoggerFactory.getLogger(LoggerManager.class);

    // Конфігурація для SMTP
    private static final String SENDER_EMAIL = "kvzolota159789@gmail.com";
    private static final String RECIPIENT_EMAIL = "kvzolota159789@gmail.com";


    public static void logInfo(String message) {
        logger.info(message);
    }


    public static void logWarn(String message) {
        logger.warn(message);
    }


    public static void logError(String message, Throwable exception) {
        logger.error(message, exception);
    }


    public static void logCritical(String message, Throwable exception) {
        logger.error("CRITICAL: " + message, exception);

        // Додаткові дії: надсилання email
        sendCriticalErrorEmail(message, exception);
    }


    private static void sendCriticalErrorEmail(String errorMessage, Throwable exception) {
        // Налаштування SMTP-сервера
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("kvzolota159789@gmail.com", "tlyp osus bxkh uetx");
            }
        });

        try {
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(SENDER_EMAIL));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT_EMAIL));
            emailMessage.setSubject("Критична помилка в додатку");
            emailMessage.setText("Сталася критична помилка: \n\n" + errorMessage + "\n\n" + exception.getMessage());

            Transport.send(emailMessage);
            logInfo("Повідомлення про критичну помилку надіслано на email: " + RECIPIENT_EMAIL);
        } catch (MessagingException e) {
            logError("Не вдалося надіслати повідомлення про критичну помилку на email.", e);
        }
    }
}
