package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.Teacher;

public interface TeacherMapper extends BaseMapper<Teacher> {
    @Select("SELECT info_teacher.*, info_academy.name AS academy FROM info_teacher JOIN info_academy ON info_teacher.academy_id=info_academy.id WHERE info_teacher.user_id=#{userId}")
    Teacher getTeacherByUserId(String userId);
}
