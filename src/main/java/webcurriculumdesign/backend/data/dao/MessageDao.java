package webcurriculumdesign.backend.data.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageDao {
//    @Select("SELECT `message_content` FROM `message_data` WHERE `message_name` = #{messageName} LIMIT 1")
//    String getMessageContent(String messageName);

    @Select("SELECT `message_name`, `message_content` FROM `message_data`")
    List<Map<String, String>> getAllMessageContentAsListMap();
}
