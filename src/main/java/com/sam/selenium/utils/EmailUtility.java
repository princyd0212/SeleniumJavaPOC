package com.sam.selenium.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EmailUtility {
    private static final Logger logger = Logger.getLogger(EmailUtility.class.getName());

    private static final PropertyFileReader propertyReader = new PropertyFileReader();
    private static final String FROM_EMAIL = propertyReader.getProperty("from_email");
    private static final String PASSWORD = propertyReader.getProperty("password");
    private static final String HOST = propertyReader.getProperty("smtp_host");
    private static final String recipients = propertyReader.getProperty("recipients");


    public static void sendConsolidatedEmail(List<String> testResults, String subject) {
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

        StringBuilder formattedBody = new StringBuilder();
        formattedBody.append("<html><body style='font-family: Arial, sans-serif; background-color: #f4f6f9; color: #333; margin: 0; padding: 0;'>")
                .append("<div style='max-width: 800px; margin: 0 auto; background-color: #ffffff; padding: 40px; border-radius: 8px; box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);'>")
                .append("<div style='text-align: center; margin-bottom: 20px;'>")
                .append("<img src='https://static.toiimg.com/thumb/resizemode-4,width-1280,height-720,msid-101371330/101371330.jpg' alt='Company Logo' style='width: 220px; height: auto; display: inline-block;'>")
                .append("</div>")
                .append("<h2 style='color: #007BFF; text-align: center; font-size: 30px; font-weight: 600; margin-bottom: 20px;'>Test Suite Execution Report</h2>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-bottom: 20px;'>Dear Team,</p>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-bottom: 20px;'>We are pleased to share the results of the recently executed test cases.</p>")
                .append("<table style='width: 100%; border-collapse: collapse; margin-top: 20px; font-size: 16px; border: 1px solid #ddd;'>")
                .append("<thead style='background-color: #007BFF; color: white;'>")
                .append("<tr><th style='padding: 12px; text-align: left; border-radius: 5px;'>Test Case Name</th>")
                .append("<th style='padding: 12px; text-align: left; border-radius: 5px;'>Status</th>")
                .append("<th style='padding: 12px; text-align: left; border-radius: 5px;'>Execution Time</th></tr>")
                .append("</thead><tbody>");

        List<String> failedScreenshotPaths = new ArrayList<>();

        for (String result : testResults) {
            String[] resultData = result.split(";");
            String testCaseName = resultData[0];
            String testStatus = resultData[1];
            String executionTime = resultData[2];

            // Assign color based on status
            String statusColor;
            switch (testStatus.toUpperCase()) {
                case "PASS":
                    statusColor = "#28a745"; // Green
                    break;
                case "FAIL":
                    statusColor = "#dc3545"; // Red
                    break;
                case "SKIPPED":
                    statusColor = "#ffc107"; // Orange
                    break;
                default:
                    statusColor = "#6c757d"; // Grey (for unknown statuses)
                    break;
            }

            formattedBody.append("<tr style='background-color: #f9f9f9; border-bottom: 1px solid #ddd;'>")
                    .append("<td style='padding: 12px;'>" + testCaseName + "</td>")
                    .append("<td style='padding: 12px; color: " + statusColor + "; font-weight: bold;'>" + testStatus + "</td>")
                    .append("<td style='padding: 12px;'>" + executionTime + "</td></tr>");

            // Handle failed screenshot paths
            if ("FAIL".equalsIgnoreCase(testStatus) && resultData.length > 3 && new File(resultData[3]).exists()) {
                failedScreenshotPaths.add(resultData[3]);
            }
        }

        formattedBody.append("</tbody></table>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-top: 30px;'>If you have any questions, contact us at <a href='mailto:support@yourdomain.com' style='color: #007BFF;'>support@yourdomain.com</a>.</p>")
                .append("<p style='font-size: 16px; line-height: 1.8;'>Thank you for your attention.</p>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-top: 40px;'>Best regards,</p>")
                .append("<p style='font-size: 16px; line-height: 1.8;'>Brahmbhatt Kaushal</p>")
                .append("</div></body></html>");

        // Zip screenshots if any failed tests exist
        String zipFilePath = "FailedScreenshots.zip";
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String screenshotPath : failedScreenshotPaths) {
                File screenshotFile = new File(screenshotPath);
                if (screenshotFile.exists()) {
                    try (FileInputStream fis = new FileInputStream(screenshotFile)) {
                        ZipEntry zipEntry = new ZipEntry(screenshotFile.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.severe("Error while creating ZIP file: " + e.getMessage());
        }

        // Send email
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            List<String> recipientList = Arrays.asList(recipients.split(","));
            for (String toEmail : recipientList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            }
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(formattedBody.toString(), "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(zipFile);
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);

            logger.info("Consolidated report with screenshots sent successfully to recipients: " + recipients);
        } catch (MessagingException | IOException e) {
            logger.severe("Failed to send consolidated email: " + e.getMessage());
        }
    }

}
