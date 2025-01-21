package com.sam.selenium.tests;
import utils.ConfigReader;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailUtility {
    private static final Logger logger = Logger.getLogger(EmailUtility.class.getName());
    private static final String FROM_EMAIL = ConfigReader.getProperty("from_email");
    private static final String PASSWORD = ConfigReader.getProperty("password");
    private static final String HOST = ConfigReader.getProperty("smtp_host");

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

        // Construct the email body for test failure
        String body = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f6f9; color: #333;'>"
                + "<h2 style='color: #333;'>Test Failure Report</h2>"
                + "<p style='font-size: 16px;'>Dear Team,</p>"
                + "<p style='font-size: 16px;'>The following test case has failed:</p>"
                + "<table style='width: 100%; border-collapse: collapse; margin-top: 20px;'>"
                + "<thead style='background-color: #3c8dbc; color: #fff;'>"
                + "<tr><th style='padding: 10px; text-align: left;'>Test Case ID</th><th style='padding: 10px; text-align: left;'>Test Case Name</th></tr>"
                + "</thead>"
                + "<tbody style='background-color: #ffffff;'>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>" + testCaseId + "</td><td style='padding: 10px; border: 1px solid #ddd;'>" + testCaseName + "</td></tr>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>Failed Step</td><td style='padding: 10px; border: 1px solid #ddd;'>" + failedStep + "</td></tr>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>Expected Result</td><td style='padding: 10px; border: 1px solid #ddd;'>" + expectedResult + "</td></tr>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>Actual Result</td><td style='padding: 10px; border: 1px solid #ddd;'>" + actualResult + "</td></tr>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>Error Message</td><td style='padding: 10px; border: 1px solid #ddd;'>" + errorMessage + "</td></tr>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>Severity</td><td style='padding: 10px; border: 1px solid #ddd;'>" + severity + "</td></tr>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>Test Execution Date</td><td style='padding: 10px; border: 1px solid #ddd;'>" + testExecutionDate + "</td></tr>"
                + "<tr><td style='padding: 10px; border: 1px solid #ddd;'>Test Environment</td><td style='padding: 10px; border: 1px solid #ddd;'>" + testEnvironment + "</td></tr>"
                + "</tbody>"
                + "</table>"
                + "<p style='font-size: 16px;'>Please find the attached screenshot for more details.</p>"
                + "</body></html>";

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            for (String email : toEmails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            message.setSubject("Test Failure Report: " + testCaseId);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/html");

            // Attach screenshot (ensure the screenshot path is correct and exists)
            MimeBodyPart screenshotAttachment = new MimeBodyPart();
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                File screenshotFile = new File(screenshotPath);
                if (screenshotFile.exists()) {
                    screenshotAttachment.attachFile(screenshotFile);
                } else {
                    logger.warning("Screenshot file does not exist at path: " + screenshotPath);
                }
            }

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            // Only add the screenshot part if the file exists
            if (screenshotPath != null && !screenshotPath.isEmpty() && new File(screenshotPath).exists()) {
                multipart.addBodyPart(screenshotAttachment);
            }

            message.setContent(multipart);
            Transport.send(message);
            logger.info("Failure notification sent successfully to recipients: " + toEmails);
        } catch (Exception e) {
            logger.severe("Failed to send email: " + e.getMessage());
        }
    }



    // New method to send a consolidated email with the results of all tests
    public static void sendConsolidatedEmail(List<String> toEmails, List<String> testResults,
                                             String subject, String body, List<String> screenshotPaths) {
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
        // Create the detailed body for the email (formatted HTML)
        StringBuilder formattedBody = new StringBuilder();
        formattedBody.append("<html><body style='font-family: Arial, sans-serif; background-color: #f4f6f9; color: #333; margin: 0; padding: 0;'>")
                .append("<div style='max-width: 800px; margin: 0 auto; background-color: #ffffff; padding: 40px; border-radius: 8px; box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);'>")
                .append("<div style='text-align: center; margin-bottom: 20px;'>")
                .append("<img src='https://static.toiimg.com/thumb/resizemode-4,width-1280,height-720,msid-101371330/101371330.jpg' alt='Company Logo' style='width: 220px; height: auto; display: inline-block;'>")
                .append("</div>")
                .append("<h2 style='color: #007BFF; text-align: center; font-size: 30px; font-weight: 600; margin-bottom: 20px;'>Test Suite Execution Report</h2>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-bottom: 20px;'>Dear Team,</p>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-bottom: 20px;'>We are pleased to share the results of the recently executed test cases. Below is the detailed report outlining the status of each test case, including execution time and error messages where applicable.</p>")
                // New section for summary (total tests, passed, failed, etc.)
                // Table for test results
                .append("<table style='width: 100%; border-collapse: collapse; margin-top: 20px; font-size: 16px; border: 1px solid #ddd;'>")
                .append("<thead style='background-color: #007BFF; color: white;'>")
                .append("<tr><th style='padding: 12px; text-align: left; border-radius: 5px;'>Test Case ID</th>")
                .append("<th style='padding: 12px; text-align: left; border-radius: 5px;'>Test Case Name</th>")
                .append("<th style='padding: 12px; text-align: left; border-radius: 5px;'>Status</th>")
                .append("<th style='padding: 12px; text-align: left; border-radius: 5px;'>Execution Time</th>")
                .append("<th style='padding: 12px; text-align: left; border-radius: 5px;'>Error Message</th></tr>")
                .append("</thead><tbody>");
        // Add test case details dynamically to the table
        for (String result : testResults) {
            String[] resultData = result.split(";");
            String statusColor = resultData[2].equals("PASS") ? "#28a745" : (resultData[2].equals("FAIL") ? "#dc3545" : "#ffc107");
            formattedBody.append("<tr style='background-color: #f9f9f9; border-bottom: 1px solid #ddd;'>")
                    .append("<td style='padding: 12px;'>" + resultData[0] + "</td>")
                    .append("<td style='padding: 12px;'>" + resultData[1] + "</td>")
                    .append("<td style='padding: 12px; color: " + statusColor + ";'>" + resultData[2] + "</td>")
                    .append("<td style='padding: 12px;'>" + resultData[3] + "</td>")
                    .append("<td style='padding: 12px;'>" + (resultData.length > 4 ? resultData[4] : "N/A") + "</td></tr>");
        }
        formattedBody.append("</tbody></table>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-top: 30px;'>For more detailed logs and further analysis, please click the button below:</p>")
                .append("<div style='text-align: center; margin-top: 20px;'>")
                .append("<a href='http://172.16.1.223:53779/index.html' style='background-color: #007BFF; color: white; padding: 12px 24px; font-size: 16px; text-decoration: none; border-radius: 5px; display: inline-block; transition: background-color 0.3s;'>View Detailed Logs</a>")
                .append("</div>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-top: 30px;'>If you have any questions or need additional information, please do not hesitate to contact us at <a href='mailto:support@yourdomain.com' style='color: #007BFF;'>support@yourdomain.com</a>.</p>")
                .append("<p style='font-size: 16px; line-height: 1.8;'>Thank you for your attention.</p>")
                .append("<p style='font-size: 16px; line-height: 1.8; margin-top: 40px;'>Best regards,</p>")
                .append("<p style='font-size: 16px; line-height: 1.8;'>Brahmbhatt Kaushal</p>")
                .append("<hr style='border: 0; border-top: 1px solid #ddd; margin-top: 30px;'>")
                .append("<footer style='text-align: center; font-size: 14px; color: #777; padding-top: 20px;'>")
                .append("<p>If you no longer wish to receive these reports, you can <a href='https://yourdomain.com/unsubscribe' style='color: #007BFF;'>unsubscribe</a>.</p>")
                .append("<p>Company Name | Address | Phone Number | <a href='https://yourdomain.com/privacy-policy' style='color: #007BFF;'>Privacy Policy</a></p>")
                .append("</footer>")
                .append("</div></body></html>");
        // Send email
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            for (String toEmail : toEmails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            }
            message.setSubject(subject);
            // Add body as a part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(formattedBody.toString(), "text/html");
            // Add attachments for screenshots
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            if (screenshotPaths != null && !screenshotPaths.isEmpty()) {
                for (String screenshotPath : screenshotPaths) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    File file = new File(screenshotPath);
                    if (file.exists()) {
                        attachmentPart.attachFile(file);
                        multipart.addBodyPart(attachmentPart);
                    } else {
                        logger.warning("Screenshot file not found: " + screenshotPath);
                    }
                }
            }
            message.setContent(multipart);
            Transport.send(message);
            logger.info("Consolidated report with screenshots sent successfully to recipients: " + toEmails);
        } catch (Exception e) {
            logger.severe("Failed to send consolidated email with screenshots: " + e.getMessage());
        }
    }
}
