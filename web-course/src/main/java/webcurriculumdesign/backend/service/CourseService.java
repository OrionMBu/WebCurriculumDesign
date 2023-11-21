package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.po.Course;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.CourseMapper;
import webcurriculumdesign.backend.util.CourseUtil;

import java.util.*;

@Service
public class CourseService {
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
}
