package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.Student;

import java.util.List;
import java.util.Map;

public interface StudentMapper extends BaseMapper<Student> {
    @Select("SELECT info_student.*, info_academy.name AS academy FROM info_student JOIN info_academy ON info_student.academy_id = info_academy.id WHERE info_student.user_id=#{userId}")
    Student getStudentByUserId(String userId);

    @Select("SELECT class_number AS `class`, COUNT(class_number) AS number " +
            "FROM info_student " +
            "GROUP BY class_number")
    List<Map<String, Object>> countStudentNumber();
}
