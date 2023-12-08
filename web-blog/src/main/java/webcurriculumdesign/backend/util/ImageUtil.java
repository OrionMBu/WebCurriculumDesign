package webcurriculumdesign.backend.util;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.dto.FileToUpload;
import webcurriculumdesign.backend.exception.FileUploadException;
import webcurriculumdesign.backend.service.BaseService;

@Component
public class ImageUtil {
    @Resource
    BaseService baseService;

    public String uploadImage(MultipartFile file, String folderName, String fileName, boolean isBaseFile) throws Exception {
        // 判断是否为图片
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileUploadException("请上传图片");
        }

        // 获取文件内容
        byte[] fileBytes = file.getBytes();

        // 获取上传文件的原始文件名
        String originalFilename = file.getOriginalFilename();

        // 从原始文件名中提取文件扩展名
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // 保存图片
        return baseService.uploadFile(new FileToUpload(fileBytes, CurrentUser.userMail, folderName, fileName + fileExtension, isBaseFile));
    }
}
