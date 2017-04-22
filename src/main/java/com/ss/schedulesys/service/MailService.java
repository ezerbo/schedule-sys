package com.ss.schedulesys.service;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.ss.schedulesys.config.ScheduleSysProperties;
import com.ss.schedulesys.domain.ScheduleSysUser;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Slf4j
@Service
public class MailService {


    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";

    private ScheduleSysProperties scheduleSysProperties;
    private JavaMailSenderImpl javaMailSender;
    private SpringTemplateEngine templateEngine;
    
    @Autowired
    public MailService(ScheduleSysProperties scheduleSysProperties, JavaMailSenderImpl javaMailSender,
    		SpringTemplateEngine templateEngine) {
    	this.javaMailSender = javaMailSender;
    	this.templateEngine = templateEngine;
    	this.scheduleSysProperties = scheduleSysProperties;
	}

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(scheduleSysProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendActivationEmail(ScheduleSysUser user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmailAddress());
        Context context = new Context();
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("account-activation-email", context);
        String subject = scheduleSysProperties.getMail().getAccountActivationEmailSubject();
        sendEmail(user.getEmailAddress(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(ScheduleSysUser user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmailAddress());
        Context context = new Context(Locale.ENGLISH);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("password-reset-email", context);
        String subject = scheduleSysProperties.getMail().getPasswordResetEmailSubject();
        sendEmail(user.getEmailAddress(), subject, content, false, true);
    }
}