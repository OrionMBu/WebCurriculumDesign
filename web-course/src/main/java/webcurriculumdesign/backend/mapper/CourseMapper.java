package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import webcurriculumdesign.backend.data.dto.ScoreDto;
import webcurriculumdesign.backend.data.po.Course;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 通过教师姓名等信息查询课程
     *
     * @param courseName 课程名称
     * @param index 课序号
     * @param teacherName 教师姓名
     * @param type 课程类型
     * @param rowBounds 分页设置 new RowBounds(offset, pageSize)
     * @return List<Course>
     */
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course_teacher " +
            "INNER JOIN course ON course_teacher.course_id = course.id " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "INNER JOIN info_teacher ON course_teacher.user_id = info_teacher.user_id " +
            "WHERE course.name like '${courseName}' AND course.`index` like '${index}' AND info_teacher.name like '${teacherName}' AND course.type like '${type}'")
    List<Course> searchCourseWithTeacherName(@Param("courseName")String courseName, @Param("index")String index, @Param("teacherName")String teacherName, @Param("type")String type, RowBounds rowBounds);

    /**
     * 查询课程
     *
     * @param courseName 课程名称
     * @param index 课序号
     * @param type 课程类型
     * @param rowBounds 分页设置 new RowBounds(offset, pageSize)
     * @return List<Course>
     */
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "WHERE course.name like '${courseName}' AND course.`index` like '${index}' AND course.type like '${type}'")
    List<Course> searchCourseWithoutTeacher(@Param("courseName")String courseName, @Param("index")String index, @Param("type")String type, RowBounds rowBounds);

    /**
     * 查询教师课程信息
     *
     * @param userId 用户id
     * @return List<Course>
     */
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course_teacher " +
            "LEFT JOIN course ON course_teacher.course_id = course.id " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "WHERE course_teacher.user_id = #{userId}")
    List<Course> getTeacherCourse(Integer userId);

    /**
     * 查询学生课程信息
     *
     * @param userId 用户id
     * @return List<Course>
     */
    @Select("SELECT course.*, info_academy.name AS academy " +
            "FROM course_student " +
            "LEFT JOIN course ON course_student.course_id = course.id " +
            "JOIN info_academy ON info_academy.id = course.academy_id " +
            "WHERE course_student.user_id = #{userId}")
    List<Course> getStudentCourse(Integer userId);

    /**
     * 插入选课信息
     *
     * @param tableName 目标表，区分教师和学生
     * @param courseId 课程id
     * @param userId 用户id
     */
    @Insert("INSERT INTO ${tableName}(course_id, user_id) VALUES (#{courseId}, #{userId})")
    void insertCourseBinding(@Param("tableName") String tableName, @Param("courseId") Integer courseId, @Param("userId") Integer userId);

    /**
     * 删除选课信息
     *
     * @param tableName 目标表，区分教师和学生
     * @param courseId 课程id
     * @param userId 用户id
     */
    @Delete("DELETE FROM ${tableName} WHERE course_id = #{courseId} AND user_id = #{userId}")
    void deleteCourseBinding(@Param("tableName") String tableName, @Param("courseId") Integer courseId, @Param("userId") Integer userId);

    /**
     * 更新学生课程成绩，regular和finalScore不能都为-1
     * 根据权重自动更新total总成绩，保留两位小数
     *
     * @param userId 用户id
     * @param courseId 课程id
     * @param regular 平时成绩
     * @param finalScore 期末成绩
     */
    @Update("UPDATE course_student " +
            "SET " +
            "  regular = IF(#{regular} != -1, #{regular}, regular), " +
            "  final_score = IF(#{finalScore} != -1, #{finalScore}, final_score), " +
            "  total = " +
            "  ROUND(" +
            "  CASE " +
            "    WHEN #{regular} != -1 AND #{finalScore} != -1 THEN " +
            "      #{regular} * (SELECT regular_weight FROM course WHERE course.id = #{courseId}) + #{finalScore} * (SELECT final_weight FROM course WHERE course.id = #{courseId}) " +
            "    WHEN #{regular} = -1 AND regular != -1 AND #{finalScore} != -1 THEN " +
            "      regular * (SELECT regular_weight FROM course WHERE course.id = #{courseId}) + #{finalScore} * (SELECT final_weight FROM course WHERE course.id = #{courseId}) " +
            "    WHEN #{finalScore} = -1 AND final_score != -1 AND #{regular} != -1 THEN " +
            "      #{regular} * (SELECT regular_weight FROM course WHERE course.id = #{courseId}) + final_score * (SELECT final_weight FROM course WHERE course.id = #{courseId}) " +
            "    ELSE " +
            "      total " +
            "  END, 2) " +
            "WHERE user_id = #{userId} AND course_id = #{courseId}")
    void updateStudentScore(@Param("userId") Integer userId, @Param("courseId") Integer courseId, @Param("regular") Double regular, @Param("finalScore") Double finalScore);

    /**
     * 查询用户指定科目的成绩
     *
     * @param userId 用户id
     * @param courseId 课程id
     */
    @Select("SELECT `index`, name, credit, regular, final_score, total, course_id AS id, " +
            "(SELECT COUNT(total) FROM course_student cs2 " +
            "WHERE cs2.total > cs1.total AND cs2.course_id = cs1.course_id) + 1 AS ranking, " +
            "IF(total >= 60, ROUND(total / 10 - 5, 1), 0) AS point " +
            "FROM course_student cs1 " +
            "LEFT JOIN course ON cs1.course_id = course.id " +
            "WHERE user_id = #{userId} AND course_id = #{courseId}")
    ScoreDto getScoreByCourseId(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /**
     * 查询用户所有科目的成绩
     *
     * @param userId 用户id
     */
    @Select("SELECT `index`, name, credit, regular, final_score, total, course_id AS id, " +
            "(SELECT COUNT(total) FROM course_student cs2 " +
            "WHERE cs2.total > cs1.total AND cs2.course_id = cs1.course_id) + 1 AS ranking, " +
            "IF(total >= 60, ROUND(total / 10 - 5, 1), 0) AS point " +
            "FROM course_student cs1 " +
            "LEFT JOIN course ON cs1.course_id = course.id " +
            "WHERE user_id = #{userId}")
    List<ScoreDto> getScoreListByUserId(@Param("userId") Integer userId);

    /**
     * 更新学生GPA
     *
     * @param userId 用户id
     */
    @Update("UPDATE info_student SET GPA = #{GPA} WHERE user_id = #{userId}")
    void updateStudentGPA(@Param("userId") Integer userId, @Param("GPA") Double GPA);

    /**
     * 获取学生GPA和GPA排名信息
     *
     * @param userId 用户id
     */
    @Select("SELECT `GPA`, " +
            "(SELECT COUNT(GPA) FROM info_student is2 " +
            "WHERE is2.GPA > is1.GPA) + 1 AS ranking " +
            "FROM info_student is1 " +
            "WHERE user_id = #{userId}")
    Map<String, Object> getStudentGPA(Integer userId);
}
