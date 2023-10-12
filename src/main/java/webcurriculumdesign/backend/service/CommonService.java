package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.dao.StaticValueDao;
import webcurriculumdesign.backend.data.enums.Constant;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.exception.FileUploadException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CommonService {
    @Resource
    StaticValueDao staticValueDao;

    /**
     * 上传文件
     *
     * @param file 文件
     * @param folderName 文件夹名（服务名）
     * @param fileName 文件名
     * @return 文件路径
     * @throws IOException IO异常
     * @throws FileUploadException 文件异常
     */
    public String uploadFile(MultipartFile file, String folderName, String fileName) throws IOException, FileUploadException {
        // 判断文件是否为空
        if (file.isEmpty()) {
            throw new FileUploadException("上传文件为空");
        }

        byte[] fileBytes;
        try {
            // 获取文件内容
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new FileUploadException("获取文件异常");
        }

        // 获取文件地址
        StringBuilder builder = new StringBuilder(staticValueDao.getValue("file_path"));

        // 以邮箱创建文件地址
        File parentFile = new File(builder.append("/").append(CurrentUser.userMail).toString());
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new FileUploadException("父文件夹创建失败");

        // 创建文件夹
        File folder = new File(builder.append("/").append(folderName).toString());
        if (!folder.exists() && !folder.mkdirs())
            throw new FileUploadException("子文件夹创建失败");

        // 判断文件是否存在
        File specificFile = new File(builder.append("/").append(fileName).toString());

        // 将文件保存到指定目录
        Files.write(Paths.get(specificFile.getPath()), fileBytes);

        return Constant.PREFIX_URL + "/" + CurrentUser.userMail + "/" + folderName + "/" + fileName;
    }
}
