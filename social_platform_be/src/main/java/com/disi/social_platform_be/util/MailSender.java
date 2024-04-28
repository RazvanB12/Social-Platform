package com.disi.social_platform_be.util;

import com.disi.social_platform_be.exception.DuplicateEmailException;
import com.disi.social_platform_be.model.PasswordResetToken;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.repository.IPasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MailSender {

    private final JavaMailSender javaMailSender;
    private final IPasswordResetTokenRepository tokenRepository;
    @Value("${spring.mail.reset-url}")
    private String RESET_PASSWORD_URL;
    @Value("${spring.mail.email-address}")
    private String EMAIL_ADDRESS;
    private final String BAD_GATEWAY = "Failed to send the reset email";
    private final String SUCCESS = "Email was send successfully";

    public MailSender(JavaMailSender javaMailSender, IPasswordResetTokenRepository tokenRepository) {
        this.javaMailSender = javaMailSender;
        this.tokenRepository = tokenRepository;
    }

    public String sendEmail(User user) {
        try {
            String resetLink = generateResetLink(user);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(EMAIL_ADDRESS);
            message.setTo(user.getEmail());
            message.setSubject("Reset password");
            message.setText("Hello \n \n" + "To reset your account password, click on the link below: \n" + resetLink);
            javaMailSender.send(message);
            return SUCCESS;
        } catch (Exception e) {
            throw new DuplicateEmailException(BAD_GATEWAY);
        }
    }

    private String generateResetLink(User user) {
        UUID tokenId = UUID.randomUUID();
        LocalDateTime expireDateTime = LocalDateTime.now().plusMinutes(30);

        tokenRepository.findByUserToken(user)
                .ifPresentOrElse(foundToken -> {
                            foundToken.setToken(tokenId.toString());
                            foundToken.setExpireDateTime(expireDateTime);
                            tokenRepository.save(foundToken);
                        },
                        () -> {
                            PasswordResetToken resetToken = new PasswordResetToken();
                            resetToken.setUserToken(user);
                            resetToken.setToken(tokenId.toString());
                            resetToken.setExpireDateTime(expireDateTime);
                            tokenRepository.save(resetToken);
                        });

        return RESET_PASSWORD_URL + tokenId;
    }
}
