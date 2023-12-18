package webcurriculumdesign.backend.data.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AcademyDao {
    /**
     * 获取学院列表
     *
     */
    @Select("SELECT * FROM info_academy")
    List<Map<String, String>> getAcademyList();

    /**
     * 插入新学院
     *
     * @param academy 学院名称
     */
    @Insert("INSERT INTO info_academy VALUES (NULL, #{academy})")
    void insertAcademy(@Param("academy") String academy);

    /**
     * 删除学院
     *
     * @param academyId 学院id
     */
    @Delete("DELETE FROM info_academy WHERE id = #{academyId}")
    void deleteById(@Param("academyId") Integer academyId);
}
