package com.wpp.devtools.util;

import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: volvo-server
 * @description:
 * @author: wpp
 * @create: 2020-07-07
 **/
@Component
public class EmailUtil {

    // 发件人邮箱地址
    @Value("${Email.from}")
    private String from;

    // HOST地址
    @Value("${Email.host}")
    private String host;

    // 端口
    @Value("${Email.port}")
    private String port;


    // 发件人邮箱客户端授权码
    @Value("${Email.password}")
    private String password;

    /**
     * 发送验证信息的邮件
     *
     * @param to
     * @param text
     * @param title
     */
    public void sendMail(String to, String title, String text) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", host); // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host); // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", true); // 用刚刚设置好的props对象构建一个session
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.port", port);
        Session session = Session.getDefaultInstance(props); // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(false); // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session); // 加载发件人地址
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 加载收件人地址
            message.setSubject(title); // 加载标题
            Multipart multipart = new MimeMultipart(); // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            BodyPart contentPart = new MimeBodyPart(); // 设置邮件的文本内容
            contentPart.setContent(text, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            message.saveChanges(); // 保存变化
            Transport transport = session.getTransport("smtp"); // 连接服务器的邮箱
            transport.connect(host, from, password); // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            throw new CustomException(ExceptionCodeEnums.EMAIL_SEND_ERROR);
        }
    }
}
