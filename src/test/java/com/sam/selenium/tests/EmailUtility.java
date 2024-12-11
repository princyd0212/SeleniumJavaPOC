package com.sam.selenium.tests;


    import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

    public class EmailUtility {

        public static void sendEmail(String toEmail, String subject, String body) {
            String fromEmail = "kaushalbtridhyatech@gmail.com"; // Your email address
            String password = "ubmhsgxngdjiwcxe"; // Your email password
            String host = "smtp.gmail.com"; // SMTP host (e.g., Gmail)

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                System.out.println("Email sent successfully");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }


