package com.sam.selenium.tests;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailUtility {

    private static final Logger logger = Logger.getLogger(EmailUtility.class.getName());
    private static final String FROM_EMAIL = "kaushalbtridhyatech@gmail.com";
    private static final String PASSWORD = "ubmhsgxngdjiwcxe";
    private static final String HOST = "smtp.gmail.com";

    public static void sendEmail(List<String> toEmails, String testCaseId, String testCaseName,
                                 String failedStep, String expectedResult, String actualResult,
                                 String errorMessage, String testSteps, String severity,
                                 String testExecutionDate, String testEnvironment,
                                 String screenshotPath) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        // Construct the email body
        String body = "<html><body style='font-family: Helvetica, Arial, sans-serif; background-color: #f7f7f7;'>"
                + "<h2 style='color: #2d3e50;'>Test Failure Report</h2>"
                + "<p>Dear Team,</p>"
                + "<p>The following test case has failed:</p>"
                + "<table style='width: 100%; border-collapse: collapse; border: 1px solid #ddd;'>"
                + "<tr><th>Test Case ID</th><td>" + testCaseId + "</td></tr>"
                + "<tr><th>Test Name</th><td>" + testCaseName + "</td></tr>"
                + "<tr><th>Failed Step</th><td>" + failedStep + "</td></tr>"
                + "<tr><th>Expected Result</th><td>" + expectedResult + "</td></tr>"
                + "<tr><th>Actual Result</th><td>" + actualResult + "</td></tr>"
                + "<tr><th>Error Message</th><td>" + errorMessage + "</td></tr>"
                + "<tr><th>Severity</th><td>" + severity + "</td></tr>"
                + "<tr><th>Test Execution Date</th><td>" + testExecutionDate + "</td></tr>"
                + "<tr><th>Test Environment</th><td>" + testEnvironment + "</td></tr>"
                + "</table>"
                + "<p>Please find the attached screenshot for more details.</p>"
                + "</body></html>";

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));

            // Add multiple recipients
            for (String toEmail : toEmails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            }

            message.setSubject("Test Failure Report: " + testCaseId);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/html");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                File file = new File(screenshotPath);
                attachmentPart.attachFile(file);
            }

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);
            logger.info("Failure notification sent successfully to recipients: " + toEmails);
        } catch (Exception e) {
            logger.severe("Failed to send email: " + e.getMessage());
        }
    }
}
