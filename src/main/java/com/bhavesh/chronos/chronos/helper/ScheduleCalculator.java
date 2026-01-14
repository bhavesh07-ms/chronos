package com.bhavesh.chronos.chronos.helper;

import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.entities.JobExecution;
import com.bhavesh.chronos.chronos.entities.JobLog;
import com.bhavesh.chronos.chronos.entities.repo.JobLogRepository;
import com.bhavesh.chronos.chronos.enums.RecurringSchedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;


@Component
public class ScheduleCalculator {

    private final JobLogRepository logRepository;


    @Value("${job.email.username}")
    private String username;
    @Value("${job.email.password}")
    private String password;

    public ScheduleCalculator(JobLogRepository logRepository) {
        this.logRepository = logRepository;
    }
    public LocalDateTime calculateNextRun(
            RecurringSchedule schedule,
            LocalDateTime fromTime) {

        return switch (schedule) {
            case HOURLY -> fromTime.plusHours(1);
            case DAILY -> fromTime.plusDays(1);
            case WEEKLY -> fromTime.plusWeeks(1);
            case MONTHLY -> fromTime.plusMonths(1);
        };
    }

    public void executeEmail(Job job, JobExecution execution) {

        // JavaMail properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your SMTP server
        props.put("mail.smtp.port", "465"); // Replace with your SMTP port

        // Debugging enabled
        props.put("mail.debug", "true");

        // SSL configuration for port 465
        props.put("mail.smtp.ssl.enable", "true");

        // Fetch username and password from environment variables


        // Check for missing credentials
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalStateException("Email credentials are missing or invalid. Ensure EMAIL_USERNAME and EMAIL_PASSWORD are set.");
        }

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(username));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(job.getToEmail()));

            // Set Subject: header field
            message.setSubject(job.getSubject());

            // Set the actual message
            message.setText(job.getBody());

            // Send message
            log(execution, "Attempting to send email to: " + job.getToEmail());
            Transport.send(message);
            log(execution, "Email sent successfully to: " + job.getToEmail());
        } catch (MessagingException e) {
            log(execution, "Failed to send email: " + e.getMessage());
            log(execution, "SMTP Host: " + props.getProperty("mail.smtp.host"));
            log(execution, "SMTP Port: " + props.getProperty("mail.smtp.port"));
            log(execution, "SMTP Auth: " + props.getProperty("mail.smtp.auth"));
            log(execution, "SMTP StartTLS: " + props.getProperty("mail.smtp.starttls.enable"));
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private void log(JobExecution execution, String message) {
        JobLog log = new JobLog();
        log.setJobExecution(execution);
        log.setLogMessage(message);
        logRepository.save(log);
    }
}
