package com.akane.tools.utils.smtp;

import com.akane.tools.config.EmailConfig;
import com.akane.tools.config.SMTPConfig;
import com.akane.tools.model.email.AttachProp;
import com.akane.tools.model.email.SendMailProp;
import com.akane.tools.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * SMTPEmailUtils
 *
 * @author akane
 */
@Slf4j
public class SMTPEmailUtils
{
    private static String sender;
    private static String smtpHost;
    private static Boolean ssl;
    private static Boolean auth;
    private static String smtpUser;
    private static String smtpPass;
    private static Properties properties;
    private static Session session;
    private static MimeMessage message;
    private static MailAuthenticator authenticator;

    static {
        // 获取配置参数
        sender = EmailConfig.getSender();
        smtpHost = SMTPConfig.getHost();
        ssl = SMTPConfig.getSsl();
        auth = SMTPConfig.getAuth();
        smtpUser = SMTPConfig.getUser();
        smtpPass = SMTPConfig.getPass();
        // 设置属性参数
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", smtpHost);
        if(ssl) {
            properties.setProperty("mail.smtp.socketFactory.port", "465");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.port", "465");
        }
        if(auth) {
            properties.setProperty("mail.smtp.auth", "true");
        }
    }

    /**
     * 发送邮件
     * @param sendMailProp 邮件信息
     */
    public static void sendEmail(SendMailProp sendMailProp) throws MessagingException,
            UnsupportedEncodingException
    {
        if(auth) {
            authenticator = new MailAuthenticator(smtpUser, smtpPass);
        }
        // Get the default Session object
        session = Session.getDefaultInstance(properties, authenticator);
        // Create a default MimeMessage object
        message = new MimeMessage(session);
        // Set From: header field of the header.
        message.setFrom(new InternetAddress(sender));
        // Set To: header field of the header.
        setReceivers(sendMailProp.getTo(), Message.RecipientType.TO);
        // Set CC: header field of the header.
        setReceivers(sendMailProp.getCc(), Message.RecipientType.CC);
        // Set BCC: header field of the header.
        setReceivers(sendMailProp.getBcc(), Message.RecipientType.BCC);
        // Set Subject: header field
        message.setSubject(sendMailProp.getSubject());
        // Create a multipart message
        Multipart multipart = new MimeMultipart();
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();
        // Fill the message
        if(ObjectUtils.isNotEmpty(sendMailProp.getContent())) {
            messageBodyPart.setContent(sendMailProp.getContent(), sendMailProp.getMimeType());
        }
        // Add message part
        multipart.addBodyPart(messageBodyPart);
        // Add attachment part
        if(ObjectUtils.isNotEmpty(sendMailProp.getAttachment())) {
            for (AttachProp attachProp : sendMailProp.getAttachment()) {
                BodyPart attachBodyPart = getAttachBodyPart(attachProp);
                multipart.addBodyPart(attachBodyPart);
            }
        }
        // Send the complete message parts
        message.setContent(multipart);
        // Send message
        Transport.send(message);
    }

    /**
     * 设置邮件收件人
     * @param receivers
     * @param type
     */
    private static void setReceivers(List<String> receivers, Message.RecipientType type) throws MessagingException
    {
        if(ObjectUtils.isNotEmpty(receivers)) {
            for (String receiver : receivers) {
                message.addRecipient(type, new InternetAddress(receiver));
            }
        }
    }

    /**
     * 创建附件对象
     * @param attachProp
     * @return
     * @throws MessagingException
     */
    private static BodyPart getAttachBodyPart(AttachProp attachProp) throws MessagingException, UnsupportedEncodingException
    {
        BodyPart attachBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachProp.getFilePath());
        attachBodyPart.setDataHandler(new DataHandler(source));
        attachBodyPart.setFileName(MimeUtility.encodeText(attachProp.getFileName()));
        return attachBodyPart;
    }

}
