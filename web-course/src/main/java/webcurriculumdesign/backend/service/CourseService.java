package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.constant.Role;
import webcurriculumdesign.backend.data.po.Course;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.CourseMapper;
import webcurriculumdesign.backend.util.CourseUtil;

import java.util.*;

@Service
public class CourseService {
    @Resource
    UserService userService;
    @Resource
    CourseMapper courseMapper;
    @Resource
    CourseUtil courseUtil;

    // 查询课表
    public Result getCourseList() {
        List<Course> courseList;
        switch (CurrentUser.role) {
            case "TEACHER" -> {
                courseList = courseMapper.getTeacherCourse(CurrentUser.id);
                courseUtil.fillCourseData(courseList);
            }
            case "STUDENT" -> {
                courseList = courseMapper.getStudentCourse(CurrentUser.id);
                courseUtil.fillCourseData(courseList);
            }
            default -> {
                return Result.ok();
            }
        }
        return Result.success(courseList);
    }

    // 查询课程
    public Result searchCourse(int page, int pageSize, String courseName, String index, String teacherName, int type) {
        int offset = (page - 1) * pageSize;
        String typeChange = type == 3 ? "%" : String.valueOf(type);

        // 查询课程列表
        List<Course> courseList;
        if (!teacherName.isEmpty()) {
            courseList = courseMapper.searchCourseWithTeacherName("%" + courseName + "%", "%" + index + "%", "%" + teacherName + "%", typeChange, new RowBounds(offset, pageSize));
        } else {
            courseList = courseMapper.searchCourseWithoutTeacher("%" + courseName + "%", "%" + index + "%", typeChange, new RowBounds(offset, pageSize));
        }

        // 填充课程上课时间和教师信息
        courseUtil.fillCourseData(courseList);

        return Result.success(courseList);
    }

    // 选课
    public Result selectCourse(int courseId, int userId) {
        // 管理员操作
        if (userId != 0 && CurrentUser.role.equals(Role.ADMIN.role)) {
            Map<String, Object> userData;
            try {
                userData = userService.getUserData(userId);
            } catch (Exception e) {
                return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
            }

            CurrentUser.role = (String) userData.get("role");
            CurrentUser.id = (Integer) userData.get("id");
        }

        // 插入数据
        try {
            switch (CurrentUser.role) {
                case "TEACHER" -> courseMapper.insertCourseData("course_teacher", courseId, CurrentUser.id);
                case "STUDENT" -> courseMapper.insertCourseData("course_student", courseId, CurrentUser.id);
            }
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
