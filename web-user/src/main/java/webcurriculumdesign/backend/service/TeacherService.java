package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.po.Teacher;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.TeacherMapper;

@Service
public class TeacherService {
    @Resource
    TeacherMapper teacherMapper;

    // 获取教师信息
    public Result getTeacherInfoByUserId(String userId) {
        return Result.success(teacherMapper.getTeacherByUserId(userId));
    }

    // 插入教师信息（初始化）
    @Transactional
    public void insertTeacherInfo(Integer userId) {
        Teacher teacher = new Teacher();
        teacher.setUserId(userId);
        teacherMapper.insert(teacher);
    }
}
