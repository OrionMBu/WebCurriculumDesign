package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * from user where mail=#{account} or name=#{account}")
    User getUser(String account);
}
