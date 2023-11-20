package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.dao.CourseTeacherDao;
import webcurriculumdesign.backend.data.dto.CourseTimeDto;
import webcurriculumdesign.backend.data.po.Course;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.CourseMapper;
import webcurriculumdesign.backend.mapper.CourseTimeMapper;

import java.util.*;

@Service
public class CourseService {
    @Resource
    CourseMapper courseMapper;
    @Resource
    CourseTimeMapper courseTimeMapper;
    @Resource
    CourseTeacherDao courseTeacherDao;

    // 查询课程
    public Result getCourseList(int page, int pageSize, String courseName, String index, String teacherName, int type) {
        int offset = (page - 1) * pageSize;
        String typeChange = type == 3 ? "%" : String.valueOf(type);

        // 查询课程列表
        List<Course> courseList;
        if (!teacherName.isEmpty()) {
            courseList = courseMapper.searchCourseWithTeacherName("%" + courseName + "%", "%" + index + "%", "%" + teacherName + "%", typeChange, new RowBounds(offset, pageSize));
        } else {
            courseList = courseMapper.searchCourseWithoutTeacher("%" + courseName + "%", "%" + index + "%", typeChange, new RowBounds(offset, pageSize));
        }

        // 生成course_id和教师的映射
        Map<Integer, List<String>> courseTeacherMap = getCourseTeacherMap();

        // 拼接数据
        for (Course course : courseList) {
            course.setTeacher(courseTeacherMap.get(course.getId()));

            List<CourseTimeDto> courseTimeDtoList = courseTimeMapper.selectById(course.getId())
                    .stream()
                    .map(courseTime -> new CourseTimeDto(
                            Arrays.stream(courseTime.getWeek().split(",")).map(Integer::parseInt).toList(),
                            courseTime.getWeekday(),
                            courseTime.getOrder()
                    ))
                    .toList();

            course.setCourseTimeList(courseTimeDtoList);
        }

        return Result.success(courseList);
    }

    // 获取课程-教师关系
    public Map<Integer, List<String>> getCourseTeacherMap() {
        // 查询所有课程教师关系
        List<Map<String, Object>> courseTeacherList = courseTeacherDao.selectAllCourseTeacher();

        // 生成course_id和教师的映射
        Map<Integer, List<String>> courseTeacherMap = new HashMap<>();
        for (Map<String, Object> courseTeacher : courseTeacherList) {
            int courseId = (int) courseTeacher.get("course_id");
            String teacher_name = (String) courseTeacher.get("name");

            courseTeacherMap.computeIfAbsent(courseId, x -> new ArrayList<>()).add(teacher_name);
        }
        return courseTeacherMap;
    }
}
