package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    StudentService studentService;

    @RequiredLogin(roles = "ALL")
    @GetMapping("/getAuditResult")
    public Result getAuditResult() {
        return studentService.getAuditResult();
    }
}
