package webcurriculumdesign.backend.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.dao.CourseDao;
import webcurriculumdesign.backend.data.dto.CourseAudit;
import webcurriculumdesign.backend.data.dto.CourseTimeDto;
import webcurriculumdesign.backend.data.po.AuditRecord;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AuditMapper;

import java.util.stream.Collectors;

@Service
public class AuditService {
    @Resource
    AuditMapper auditMapper;
    @Resource
    CourseDao courseDao;

    // 获取审批事件
    public Result getAudit() {
        return Result.success(auditMapper.getAudit());
    }

    // 添加审批
    public Result addAudit(String auditName, int prompt) {
        try {
            auditMapper.addAudit(auditName, prompt);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 删除审批事件
    @Transactional
    public Result deleteAudit(int auditId) {
        auditMapper.deleteRecordByAuditId(auditId);
        auditMapper.deleteAuditById(auditId);
        return Result.ok();
    }

    // 获取用户申请结果
    public Result getAuditResult() {
        // 查询结果
        return Result.success(auditMapper.getAuditByApplicantId(CurrentUser.id));
    }

    // 更新审批状态
    @Transactional
    public Result updateAuditStatus(int recordId, int status) {
        if (status == 3) {
            // 读取审批事件
            AuditRecord auditRecord = auditMapper.getAuditByRecordId(recordId);

            if (auditRecord.getPrompt() == 1) {
                // 获取课程
                CourseAudit courseAudit = JSON.parseObject(auditRecord.getContent(), CourseAudit.class);
                addCourse(courseAudit);
            }
        }

        // 更新审批状态
        auditMapper.updateStatusById(recordId, status);
        return Result.ok();
    }

    // 开课申请
    public Result courseApply(CourseAudit course) {
        // 判断合规性
        if (course.getFinalWeight() < 0 || course.getFinalWeight() > 1) return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "权重非法");

        // 设置为选修课
        course.setType(2);

        try {
            // 插入课程
            auditMapper.insertAudit(course.getAuditId(), JSONObject.from(course).toString(), CurrentUser.id, 8, 1);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
        }
    }

    // 添加课程
    @Transactional
    public void addCourse(CourseAudit course) throws RuntimeException{
        // 插入课程信息
        courseDao.insertCourse(course.getName(), course.getLocation(), course.getType(), course.getCredit(), course.getAcademy(), course.getFinalWeight());

        // 获取最大id，更新课序号
        int id = courseDao.getMaxCourseId();
        courseDao.updateIndex(id);

        // 插入上课时间
        for (CourseTimeDto courseTimeDto : course.getCourseTimeList()) {
            String week = courseTimeDto.getWeek().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            courseDao.insertCourseTime(id, week, courseTimeDto.getWeekday(), courseTimeDto.getOrder());
        }
    }
}
