package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.po.Teacher;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.TeacherMapper;
import webcurriculumdesign.backend.util.MapUtil;

import java.util.Map;

@Service
public class TeacherService {
    @Resource
    UserService<Teacher> userService;
    @Resource
    TeacherMapper teacherMapper;
    @Resource
    MapUtil<Teacher> mapUtil;

    // 获取教师信息
    public Teacher getTeacherInfoByUserId(String userId) {
        return teacherMapper.getTeacherByUserId(userId);
    }

    // 插入教师信息（初始化）
    @Transactional
    public void insertTeacherInfo(Integer userId, String name) {
        Teacher teacher = new Teacher();
        teacher.setUserId(userId);
        teacher.setName(name);
        teacherMapper.insert(teacher);
    }

    // 更新教师信息
    public Result updateTeacherInfo(Teacher teacher, Integer userId) {
        try {
            userService.updateUserInfo(teacher, teacherMapper, userId);
            return Result.ok();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "更新错误");
        }
    }

    // 从Map获取教师
    public Teacher getTeacherFromMap(Map<String, Object> data) {
        return mapUtil.setValuesFromMap(data, Teacher.class);
    }
}
