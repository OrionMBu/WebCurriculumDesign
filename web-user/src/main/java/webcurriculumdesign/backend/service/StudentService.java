package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.po.Audit;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AuditMapper;
import webcurriculumdesign.backend.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    @Resource
    AuditMapper auditMapper;

    @Resource
    UserMapper userMapper;

    // 获取用户申请结果
    public Result getAuditResult() {
        // 查询结果
        List<Audit> auditList = auditMapper.getAuditByApplicantId(CurrentUser.id);
        List<Map<String, Object>> resultList = new ArrayList<>();

        // 处理
        for (Audit audit : auditList) {
            Map<String, Object> auditMap = new HashMap<>();
            auditMap.put("id", audit.getId());
            auditMap.put("name", audit.getName());
            auditMap.put("status", audit.getStatus());

            if (audit.getReviewer() != null) {
                User user = userMapper.selectById(audit.getReviewer());
                auditMap.put("reviewer", user.getMail());
            }

            resultList.add(auditMap);
        }

        return Result.success(resultList);
    }
}
