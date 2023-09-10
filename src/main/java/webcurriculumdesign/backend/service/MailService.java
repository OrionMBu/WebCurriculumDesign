package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.pojo.MailTemplate;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String mailSender;
    @Resource
    private JavaMailSender javaMailSender;

    @Async
    public void sendMail(MailTemplate mailTemplate) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(mailSender);
            helper.setTo(mailTemplate.getTo());
            helper.setSubject(mailTemplate.getSubject());
            helper.setText(mailTemplate.getContent(), mailTemplate.isHtml());
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
