package webcurriculumdesign.backend.data.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StaticValueDao {
    @Select("SELECT `value` FROM static_value WHERE `key` = #{key} LIMIT 1")
    String getValue(String key);
}
