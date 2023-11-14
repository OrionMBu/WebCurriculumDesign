package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.po.Student;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.StudentMapper;

@Service
public class StudentService {
    @Resource
    StudentMapper studentMapper;

    // 获取学生信息
    public Result getStudentInfoByUserId(String userId) {
        return Result.success(studentMapper.getStudentByUserId(userId));
    }

    // 插入学生信息（初始化）
    @Transactional
    public void insertStudentInfo(Integer userId) {
        Student student = new Student();
        student.setUserId(userId);
        studentMapper.insert(student);
    }
}
