package webcurriculumdesign.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传文件封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileToUpload {
    /*
    文件二进制数据
     */
    private byte[] fileBytes;

    /*
    用户邮箱
     */
    private String userMail;

    /*
    文件夹名称
     */
    private String folderName;

    /*
    文件名称
     */
    private String fileName;

    /*
    是否为基础文件
     */
    private boolean isBaseFile;
}
