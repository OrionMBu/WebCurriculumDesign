package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AuditMapper;

@Service
public class AuditService {
    @Resource
    AuditMapper auditMapper;

    // 获取用户申请结果
    public Result getAuditResult() {
        // 查询结果
        return Result.success(auditMapper.getAuditByApplicantId(CurrentUser.id));
    }
}
