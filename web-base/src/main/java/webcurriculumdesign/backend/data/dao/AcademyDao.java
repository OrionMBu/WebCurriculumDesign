package webcurriculumdesign.backend.data.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AcademyDao {
    @Select("SELECT * FROM info_academy")
    List<Map<String, String>> getAcademyList();
}
