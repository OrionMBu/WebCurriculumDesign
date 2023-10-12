package webcurriculumdesign.backend.test;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.exception.FileUploadException;
import webcurriculumdesign.backend.service.CommonService;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class LocalTestController {
    @Resource
    CommonService commonService;

    @RequiredLogin
    @PostMapping("/file")
    public String upload(@RequestParam("fileToUpload") MultipartFile file) {
        try {
            return commonService.uploadFile(file, "test", "drinking_coffee.png");
        } catch (IOException e) {
            e.printStackTrace();
            return "IO错误";
        } catch (FileUploadException e) {
            return "错误";
        }
    }
}
