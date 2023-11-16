package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.po.Student;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.StudentMapper;
import webcurriculumdesign.backend.util.MapUtil;

import java.util.Map;

@Service
public class StudentService {
    @Resource
    UserService<Student> userService;
    @Resource
    StudentMapper studentMapper;
    @Resource
    MapUtil<Student> mapUtil;

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

    // 更新学生信息
    public Result updateStudentInfo(Student student, Integer userId) {
        try {
            userService.updateUserInfo(student, studentMapper, userId);
            return Result.ok();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "更新错误");
        }
    }

    // 从Map获取学生
    public Student getStudentFromMap(Map<String, Object> data) {
        return mapUtil.setValuesFromMap(data, Student.class);
    }
}
