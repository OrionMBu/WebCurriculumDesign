package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AuditMapper;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    AuditMapper auditMapper;

    @RequiredLogin(roles = "ALL")
    @GetMapping("/getAuditResult")
    public Result getAuditResult() {
        return Result.success(auditMapper.getAuditByApplicantId(CurrentUser.id));
    }
}
