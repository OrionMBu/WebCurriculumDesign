package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.CourseTime;

import java.util.List;

@Mapper
public interface CourseTimeMapper extends BaseMapper<CourseTimeMapper> {
    @Select("SELECT * FROM course_time WHERE course_id = #{id}")
    List<CourseTime> selectById(Integer id);
}
