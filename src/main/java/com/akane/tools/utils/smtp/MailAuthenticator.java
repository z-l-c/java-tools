package com.akane.tools.utils.smtp;

import com.akane.tools.utils.ObjectUtils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * MailAuthenticator
 *
 * @author akane
 */
public class MailAuthenticator extends Authenticator
{
    private String strUser;
    private String strPwd;

    public MailAuthenticator() {
        super();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = this.strUser;
        String password = this.strPwd;
        if (ObjectUtils.isNotEmpty(username) && ObjectUtils.isNotEmpty(password)) {
            return new PasswordAuthentication(username, password);
        }
        return null;
    }

    public MailAuthenticator(String user, String password) {
        this.strUser = user;
        this.strPwd = password;
    }

}
