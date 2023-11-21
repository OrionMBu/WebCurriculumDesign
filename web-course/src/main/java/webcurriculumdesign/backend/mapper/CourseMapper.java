package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import webcurriculumdesign.backend.data.po.Course;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    // 通过教师姓名等信息查询课程
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course_teacher " +
            "INNER JOIN course ON course_teacher.course_id = course.id " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "INNER JOIN info_teacher ON course_teacher.user_id = info_teacher.user_id " +
            "WHERE course.name like '${courseName}' AND course.`index` like '${index}' AND info_teacher.name like '${teacherName}' AND course.type like '${type}'")
    List<Course> searchCourseWithTeacherName(@Param("courseName")String courseName, @Param("index")String index, @Param("teacherName")String teacherName, @Param("type")String type, RowBounds rowBounds);

    // 查询课程
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "WHERE course.name like '${courseName}' AND course.`index` like '${index}' AND course.type like '${type}'")
    List<Course> searchCourseWithoutTeacher(@Param("courseName")String courseName, @Param("index")String index, @Param("type")String type, RowBounds rowBounds);

    // 查询教师课程信息
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course_teacher " +
            "LEFT JOIN course ON course_teacher.course_id = course.id " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "WHERE course_teacher.user_id = #{userId}")
    List<Course> getTeacherCourse(Integer userId);

    // 查询学生课程信息
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course_student " +
            "LEFT JOIN course ON course_student.course_id = course.id " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "WHERE course_student.user_id = #{userId}")
    List<Course> getStudentCourse(Integer userId);
}
