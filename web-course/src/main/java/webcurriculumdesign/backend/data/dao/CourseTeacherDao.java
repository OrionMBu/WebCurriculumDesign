package webcurriculumdesign.backend.data.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseTeacherDao {
    @Select("SELECT course_id, info_teacher.name FROM course_teacher JOIN info_teacher ON course_teacher.user_id = info_teacher.user_id")
    List<Map<String, Object>> selectAllCourseTeacher();
}
