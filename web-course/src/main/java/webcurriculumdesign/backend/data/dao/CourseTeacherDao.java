package webcurriculumdesign.backend.data.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseTeacherDao {
    /**
     * 查询所有课程教师关系
     *
     */
    @Select("SELECT course_id, info_teacher.name FROM course_teacher JOIN info_teacher ON course_teacher.user_id = info_teacher.user_id")
    List<Map<String, Object>> selectAllCourseTeacher();

    /**
     * 查询课程-教师记录
     *
     * @param userId 用户id
     * @param courseId 课程id
     */
    @Select("SELECT COUNT(*) FROM course_teacher WHERE user_id = #{userId} AND course_id = #{courseId}")
    Integer getCourseTeacherRecord(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}
