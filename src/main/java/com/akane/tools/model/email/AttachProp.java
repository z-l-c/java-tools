package com.akane.tools.model.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AttachProp
 *
 * @author akane
 */
@Getter
@Setter
@NoArgsConstructor
public class AttachProp
{
    /** 附件名 */
    private String fileName;
    /** 附件完整路径 */
    private String filePath;
}
