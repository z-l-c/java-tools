package com.akane.tools.model.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * SendMailProp
 *
 * @author akane
 */
@Getter
@Setter
@NoArgsConstructor
public class SendMailProp {
    /** 收件人 */
    private List<String> to;
    /** 抄送人 */
    private List<String> cc;
    /** 暗送人 */
    private List<String> bcc;
    /** 邮件主题 */
    private String subject;
    /** 邮件内容 */
    private String content;
    /**
     * 邮件内容类型，注意编码，防止乱码
     * 纯字符类型：text/plain;charset=gbk;
     * Html类型：text/html;charset=gbk;
     */
    private String mimeType = "text/plain;charset=gbk";
    /** 附件 */
    private List<AttachProp> attachment;
}
