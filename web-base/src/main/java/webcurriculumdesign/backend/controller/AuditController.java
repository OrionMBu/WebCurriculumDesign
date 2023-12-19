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
     * 获取审批事件列表
     *
     */
    @GetMapping("/getAudit")
    public Result getAudit() {
        return auditService.getAudit();
    }

    /**
     * 添加审批事件
     *
     * @param auditName 事件名称
     * @param prompt 行为提示符（0 -> 不进行任何操作，1 -> 发布课程）
     */
    @RequiredLogin
    @PostMapping("/addAudit")
    public Result addAudit(@RequestParam String auditName, @RequestParam(required = false, defaultValue = "0") int prompt) {
        return auditService.addAudit(auditName, prompt);
    }

    /**
     * 删除审批事件
     *
     * @param auditId 审批id
     */
    @RequiredLogin
    @DeleteMapping("/deleteAudit")
    public Result deleteAudit(@RequestParam int auditId) {
        return auditService.deleteAudit(auditId);
    }

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
