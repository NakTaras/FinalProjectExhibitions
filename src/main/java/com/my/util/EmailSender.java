package com.my.util;

import com.my.db.dao.impl.LocationDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    private static final Logger logger = LogManager.getLogger(EmailSender.class);

    private final static String FROM = "exhibitions.final.project@gmail.com";

    private final static String PASSWORD = "exhibitions123";

    private final static String HOST = "smtp.gmail.com";

    public static void sendEmail(String recipientEmail, String exhibitionTopic, int amountOfTickets) {

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(FROM, PASSWORD);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(FROM));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            // Set Subject: header field
            message.setSubject("Ви купили квитки на виставку " + exhibitionTopic);

            // Now set the actual message
            message.setText("Кількість куплених квитків: " + amountOfTickets);

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            logger.error("Cannot send email!", e);
        }
    }
}
