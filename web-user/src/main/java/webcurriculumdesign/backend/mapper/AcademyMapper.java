package webcurriculumdesign.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AcademyMapper {
    @Select("SELECT info_academy.id FROM info_academy WHERE name=#{name}")
    Integer getIdByName(String name);
}
