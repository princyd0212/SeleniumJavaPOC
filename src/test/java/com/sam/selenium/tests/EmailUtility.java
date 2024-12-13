package com.sam.selenium.tests;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;
import java.util.logging.*;

public class EmailUtility {

    private static final Logger logger = Logger.getLogger(EmailUtility.class.getName());
    private static final String FROM_EMAIL = "kaushalbtridhyatech@gmail.com"; // Your email address
    private static final String PASSWORD = "ubmhsgxngdjiwcxe"; // Your email password
    private static final String HOST = "smtp.gmail.com"; // SMTP host (e.g., Gmail)

    public static void sendEmail(String toEmail, String testCaseId, String testCaseName,
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

        // Build the email body with HTML for table formatting, background color, and styling
        String body = "<html><body style='font-family: Helvetica, Arial, sans-serif; background-color: #f7f7f7;'>"
                + "<h2 style='color: #2d3e50; margin-bottom: 20px;'>Test Failure Report</h2>"
                + "<p>Dear Kartavya,</p>"
                + "<p>I hope this email finds you well. I am writing to report a failure encountered during the execution of the test case. Below are the details of the failure:</p>"

                // Test Details
                + "<h3 style='color: #2d3e50;'>Test Details:</h3>"
                + "<table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse; width: 100%; background-color: #ffffff; border: 1px solid #ddd;'>"
                + "<tr style='background-color: #e3e3e3;'><th style='text-align: left;'>Test Case ID</th><td>" + testCaseId + "</td></tr>"
                + "<tr><th style='text-align: left;'>Test Name</th><td>" + testCaseName + "</td></tr>"
                + "<tr style='background-color: #f9f9f9;'><th style='text-align: left;'>Test Execution Date</th><td>" + testExecutionDate + "</td></tr>"
                + "<tr><th style='text-align: left;'>Test Environment</th><td>" + testEnvironment + "</td></tr>"
                + "</table>"

                // Failure Details
                + "<h3 style='color: #2d3e50;'>Failure Details:</h3>"
                + "<table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse; width: 100%; background-color: #ffffff; border: 1px solid #ddd;'>"
                + "<tr style='background-color: #e3e3e3;'><th style='text-align: left;'>Failed Step</th><td>" + failedStep + "</td></tr>"
                + "<tr><th style='text-align: left;'>Expected Result</th><td>" + expectedResult + "</td></tr>"
                + "<tr style='background-color: #f9f9f9;'><th style='text-align: left;'>Actual Result</th><td>" + actualResult + "</td></tr>"
                + "<tr><th style='text-align: left;'>Error Message</th><td>" + errorMessage + "</td></tr>"
                + "<tr style='background-color: #e3e3e3;'><th style='text-align: left;'>Severity</th><td>" + severity + "</td></tr>"
                + "</table>"

                // Test Steps
                + "<h3 style='color: #2d3e50;'>Steps to Reproduce the Issue:</h3>"
                + "<pre style='background-color: #ffffff; padding: 10px; border: 1px solid #ccc;'>" + testSteps + "</pre>"
                + "<p>Please review the details above and provide assistance in resolving the issue at your earliest convenience.</p>"
                + "<p>Looking forward to your prompt response.</p>"

                // Signature
                + "<p>Best regards,<br>Brahmbhatt Kaushal R<br>QA Engineer<br>Tridhyatech<br>9537394796</p>"

                // Footer
                + "<hr style='border: 0; height: 1px; background-color: #ddd; margin: 20px 0;'>"
                + "<p style='font-size: 12px; color: #999;'>This is an automated email. If you have any questions, please contact our support team at <a href='mailto:support@tridhyatech.com'>support@tridhyatech.com</a>.</p>"
                + "</body></html>";

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Action Required: Test Failure Report for " + testCaseId);

            // Create the HTML body part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/html");

            // Create the screenshot attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                File file = new File(screenshotPath);
                attachmentPart.attachFile(file);
            }

            // Combine the text and attachment parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);  // Add the HTML body
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                multipart.addBodyPart(attachmentPart);  // Add the screenshot as attachment
            }

            // Set the message content to be multipart
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            logger.info("Failure notification sent successfully to: " + toEmail);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send email to: " + toEmail, e);
        }
    }
}
