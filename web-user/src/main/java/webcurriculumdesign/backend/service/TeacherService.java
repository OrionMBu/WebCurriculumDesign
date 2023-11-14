package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.TeacherMapper;

@Service
public class TeacherService {
    @Resource
    TeacherMapper teacherMapper;

    // 获取教师信息
    public Result getTeacherByUserId(String userId) {
        return Result.success(teacherMapper.getTeacherByUserId(userId));
    }
}
