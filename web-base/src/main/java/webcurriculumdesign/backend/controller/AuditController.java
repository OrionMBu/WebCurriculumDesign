package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.dto.CourseAudit;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.AuditService;

@RestController
@RequestMapping("/audit")
public class AuditController {
    @Resource
    AuditService auditService;

    /**
     * 获取用户申请结果
     *
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getAuditResult")
    public Result getAuditResult() {
        return auditService.getAuditResult();
    }

    /**
     * 更新审批状态
     *
     * @param recordId 审批记录id
     * @param status 目标状态
     */
    @RequiredLogin
    @PostMapping("/updateAuditStatus")
    public Result updateAuditStatus(@RequestParam int recordId, @RequestParam int status) {
        return auditService.updateAuditStatus(recordId, status);
    }


    /**
     * 课程申请
     *
     * @param course 课程
     */
    @RequiredLogin(roles = "TEACHER")
    @PostMapping("/courseApply")
    public Result courseApply(@RequestBody CourseAudit course) {
        return auditService.courseApply(course);
    }
}
