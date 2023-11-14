package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.StudentMapper;

@Service
public class StudentService {

    @Resource
    StudentMapper studentMapper;

    // 获取学生信息
    public Result getStudentInfo(String userId) {
        return Result.success(studentMapper.getStudentByUserId(userId));
    }
}
