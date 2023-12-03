package webcurriculumdesign.backend.util;

import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.dao.CourseTeacherDao;
import webcurriculumdesign.backend.data.dto.CourseTimeDto;
import webcurriculumdesign.backend.data.dto.ScoreDto;
import webcurriculumdesign.backend.data.po.Course;
import webcurriculumdesign.backend.mapper.CourseMapper;
import webcurriculumdesign.backend.mapper.CourseTimeMapper;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class CourseUtil {
    @Resource
    CourseMapper courseMapper;
    @Resource
    CourseTimeMapper courseTimeMapper;
    @Resource
    CourseTeacherDao courseTeacherDao;

    /**
     *  填充课程上课时间和教师信息
     *
     * @param courseList 课程列表
     */
    public void fillCourseData(List<Course> courseList) {
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
    }

    /**
     * 获取课程-教师关系
     *
     * @return Map 课程id : 教师列表
     */
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

    /**
     * 异步更新学生GPA
     *
     * @param userId 用户id
     */
    @Async
    public void updateStudentGPA(Integer userId) {
        // 获取学生全部课程
        List<ScoreDto> scoreDtoList = courseMapper.getScoreListByUserId(userId);

        double divided = 0;
        double divisor = 0;
        for (ScoreDto scoreDto : scoreDtoList) {
            // 排除未评完分的科目
            if (scoreDto.getRegular() == -1 || scoreDto.getFinalScore() == -1) continue;
            double point = scoreDto.getPoint();
            double credit = scoreDto.getCredit();
            divided += (point * credit);
            divisor += credit;
        }
        // 计算加权平均数
        double GPA = divided / divisor;

        // 四舍五入
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        double formattedGPA = Double.parseDouble(decimalFormat.format(GPA));

        // 更新学生GPA
        courseMapper.updateStudentGPA(userId, formattedGPA);
    }
}
