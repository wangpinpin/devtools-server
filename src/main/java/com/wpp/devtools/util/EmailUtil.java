package com.wpp.devtools.util;

import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;

import java.util.Date;
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
    @Value("${EMAIL.FROM}")
    private String from;

    // HOST地址
    @Value("${EMAIL.HOST}")
    private String host;

    // 端口
    @Value("${EMAIL.PORT}")
    private String port;


    // 发件人邮箱客户端授权码
    @Value("${EMAIL.PASSWORD}")
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

    /**
     * 发送短信验证码样式
     *
     * @param email
     * @param code
     * @return
     */
    public String sendCodeHtml(String email, String code) {
        String time = CommonUtils.getTimeStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        String html =
                "    <table style=\"\n" +
                        "    width: 538px;\n" +
                        "    background-image: linear-gradient(\n" +
                        "    to top,\n" +
                        "    #f3e7e9 0%,\n" +
                        "    #e3eeff 99%,\n" +
                        "    #e3eeff 100%\n" +
                        "    );\n" +
                        "    \" align=\"center\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                        "      <tbody>\n" +
                        "        <tr>\n" +
                        "          <td style=\"height: 65px;color: #fff;font-size: 24px;text-align: center;\">\n"
                        +
                        "            <p style=\"\n" +
                        "            color: #66c0f4;\n" +
                        "            cursor: pointer;\n" +
                        "            height: 0;\n" +
                        "            font-weight: bold;\n" +
                        "            \">小破站</p>\n" +
                        "            <p style=\"\">\n" +
                        "              <a href=\"https://wangpinpin.com\" target=\"_blank\" style=\"\n"
                        +
                        "              color: #66c0f4;\n" +
                        "              text-decoration: none;\n" +
                        "              font-size: 12px;\n" +
                        "              \">wangpinpin.com</a>\n" +
                        "            </p>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "          <td style=\"\n" +
                        "          background-image: linear-gradient(\n" +
                        "          to top,\n" +
                        "          #f3e7e9 0%,\n" +
                        "          #e3eeff 99%,\n" +
                        "          #e3eeff 100%\n" +
                        "          );\n" +
                        "          \">\n" +
                        "            <table width=\"470\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding-left: 5px; padding-right: 5px;\">\n"
                        +
                        "              <tbody>\n" +
                        "                <tr bgcolor=\"\">\n" +
                        "                  <td style=\"padding-top: 32px;\">\n" +
                        "                    <span style=\"font-size: 24px; color: #66c0f4; font-family: Arial, Helvetica, sans-serif; font-weight: bold;\">舔狗每日报道</span>\n"
                        +
                        "                    <br></td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                  <td style=\"font-size: 14px;padding-top: 16px;color: #7b7b7b;text-indent: 2em;line-height: 24px;\">您登录邮箱 "
                        + email
                        + " 所需的验证码为： <a style=\"text-decoration: none;color: #66c0f4;font-weight: bold;\">"
                        + code + "</a></td></tr>\n" +
                        "                <tr>\n" +
                        "                  <td>\n" +
                        "                    <table width=\"458\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 14px;\" bgcolor=\"\">\n"
                        +
                        "                      <tbody>\n" +
                        "                        <tr>\n" +
                        "                          <td class=\"details\">\n" +
                        "                            <br>\n" +
                        "                            <br>\n" +
                        "                            <table cellpadding=\"0\" cellspacing=\"0\" align=\"left\" border=\"0\" width=\"100%\" style=\"\n"
                        +
                        "                            text-align: right;\n" +
                        "                            \">\n" +
                        "                              <tbody style=\"\n" +
                        "                              line-height: 23px;\n" +
                        "                              \">\n" +
                        "                                <tr>\n" +
                        "                                  <td style=\"font-size: 14px; color: #c2c2c2;\">"
                        + time + "</td></tr>\n" +
                        "                                <tr>\n" +
                        "                                  <td style=\"height: 12px; line-height: 12px;\">&nbsp;</td></tr>\n"
                        +
                        "                              </tbody>\n" +
                        "                            </table>\n" +
                        "                          </td>\n" +
                        "                        </tr>\n" +
                        "                      </tbody>\n" +
                        "                    </table>\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                  <td style=\"font-size: 12px;color: #c2c2c2;padding-top: 16px;padding-bottom: 60px;\">\n"
                        +
                        "                    <br>\n" +
                        "                    <br>该验证码有效期为5分钟\n" +
                        "                    <br>\n" +
                        "                    <br>请勿将验证码展示给他人\n" +
                        "                    <br>\n" +
                        "                    <br></td></tr>\n" +
                        "              </tbody>\n" +
                        "            </table>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "      </tbody>\n" +
                        "    </table>\n";
        return html;
    }

    public String sendDiaryHtml(String content, String from, String time, String dayCount) {
        String html =
                "    <table style=\"\n" +
                        "    width: 538px;\n" +
                        "    background-image: linear-gradient(\n" +
                        "    to top,\n" +
                        "    #f3e7e9 0%,\n" +
                        "    #e3eeff 99%,\n" +
                        "    #e3eeff 100%\n" +
                        "    );\n" +
                        "    \" align=\"center\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                        "      <tbody>\n" +
                        "        <tr>\n" +
                        "          <td style=\"height: 65px;color: #fff;font-size: 24px;text-align: center;\">\n"
                        +
                        "            <p style=\"\n" +
                        "            color: #66c0f4;\n" +
                        "            cursor: pointer;\n" +
                        "            height: 0;\n" +
                        "            font-weight: bold;\n" +
                        "            \">小破站</p>\n" +
                        "            <p style=\"\">\n" +
                        "              <a href=\"https://wangpinpin.com\" target=\"_blank\" style=\"\n"
                        +
                        "              color: #66c0f4;\n" +
                        "              text-decoration: none;\n" +
                        "              font-size: 12px;\n" +
                        "              \">wangpinpin.com</a>\n" +
                        "            </p>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "          <td style=\"\n" +
                        "          background-image: linear-gradient(\n" +
                        "          to top,\n" +
                        "          #f3e7e9 0%,\n" +
                        "          #e3eeff 99%,\n" +
                        "          #e3eeff 100%\n" +
                        "          );\n" +
                        "          \">\n" +
                        "            <table width=\"470\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding-left: 5px; padding-right: 5px;\">\n"
                        +
                        "              <tbody>\n" +
                        "                <tr bgcolor=\"\">\n" +
                        "                  <td style=\"padding-top: 32px;\">\n" +
                        "                    <span style=\"font-size: 24px; color: #66c0f4; font-family: Arial, Helvetica, sans-serif; font-weight: bold;\">舔狗每日报道</span>\n"
                        +
                        "                    <br /></td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                  <td style=\"font-size: 14px;padding-top: 16px;color: #7b7b7b;text-indent: 2em;line-height: 24px;\">"
                        + content + "</td></tr>\n" +
                        "                <tr>\n" +
                        "                  <td>\n" +
                        "                    <table width=\"458\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 14px;\" bgcolor=\"\">\n"
                        +
                        "                      <tbody>\n" +
                        "                        <tr>\n" +
                        "                          <td class=\"details\">\n" +
                        "                            <br />\n" +
                        "                            <br />\n" +
                        "                            <table cellpadding=\"0\" cellspacing=\"0\" align=\"left\" border=\"0\" width=\"100%\" style=\"\n"
                        +
                        "                            text-align: right;\n" +
                        "                            \">\n" +
                        "                              <tbody style=\"\n" +
                        "                              line-height: 23px;\n" +
                        "                              \">\n" +
                        "                                <tr>\n" +
                        "                                  <td style=\"font-size: 14px; color: #c2c2c2;\">"
                        + from + "</td></tr>\n" +
                        "                                <tr>\n" +
                        "                                <tr>\n" +
                        "                                  <td style=\"font-size: 14px; color: #c2c2c2;\">"
                        + time + "</td></tr>\n" +
                        "                                <tr>\n" +
                        "                                  <td style=\"font-size: 14px; color: #c2c2c2;\">今天也是一只合格的舔狗呢</td></tr>\n"
                        +
                        "                                <tr>\n" +
                        "                                  <td style=\"font-size: 14px; color: #c2c2c2;\">舔狗的陪伴\n"
                        +
                        "                                    <span style=\"font-size: 12px;\">- 天数："
                        + dayCount + " 天</span></td>\n" +
                        "                                </tr>\n" +
                        "                                <tr>\n" +
                        "                                  <td style=\"height: 12px; line-height: 12px;\">&nbsp;</td></tr>\n"
                        +
                        "                              </tbody>\n" +
                        "                            </table>\n" +
                        "                          </td>\n" +
                        "                        </tr>\n" +
                        "                      </tbody>\n" +
                        "                    </table>\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                  <td style=\"font-size: 12px;color: #c2c2c2;padding-top: 16px;padding-bottom: 60px;cursor: pointer;\">\n"
                        +
                        "                    <br />\n" +
                        "                    <br />\n" +
                        "                    <a href=\"https://wangpinpin.com/MessageBoard\" target=\"_blank\" style=\"text-decoration: none;color: #c2c2c2;\">留言</a>\n"
                        +
                        "                    <br />\n" +
                        "                    <br />\n" +
                        "                    <a href=\"https://wangpinpin.com/MessageBoard\" target=\"_blank\" style=\"text-decoration: none;color: #c2c2c2;\">帮他人订阅</a>\n"
                        +
                        "                    <br />\n" +
                        "                    <br />\n" +
                        "                    <a href=\"https://wangpinpin.com/MessageBoard\" target=\"_blank\" style=\"text-decoration: none; color: #c2c2c2; \">点击退订</a>\n"
                        +
                        "                    <br /></td>\n" +
                        "                </tr>\n" +
                        "              </tbody>\n" +
                        "            </table>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "      </tbody>\n" +
                        "    </table>\n";
        return html;
    }
}
