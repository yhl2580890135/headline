package cn.bulletjet.headline.util;


import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import java.util.Map;
import java.util.Properties;


@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;
    @Autowired
    private Configuration freemarker;

    public boolean sendWithHTMLTemplate(String to, String subject, String template, Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        String result = null;
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarker.getTemplate(template), model));
            result = content.toString();
        } catch (Exception e) {
            logger.error("Exception occured while processing fmtemplate:" + e.getMessage(), e);
            result = "";
        }

        try {
            String nick = MimeUtility.encodeText("头条资讯");
            InternetAddress from = new InternetAddress(nick + "<2580890135@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();

        // 请输入自己的邮箱和密码，用于发送邮件
        mailSender.setUsername("2580890135@qq.com");
        mailSender.setPassword("hlhndxslvwdcebhb");
        mailSender.setHost("smtp.qq.com");
        // 请配置自己的邮箱和密码

        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
