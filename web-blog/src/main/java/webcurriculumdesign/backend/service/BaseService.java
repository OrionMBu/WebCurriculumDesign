package webcurriculumdesign.backend.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import webcurriculumdesign.backend.data.dto.FileToUpload;
import webcurriculumdesign.backend.exception.FileUploadException;

import java.io.IOException;

@FeignClient(value = "web-base", path = "/base")
public interface BaseService {

    // 上传文件
    @PostMapping("/uploadFile")
    String uploadFile(@RequestBody FileToUpload fileToUpload) throws IOException, FileUploadException;
}
