package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * from info_user where mail=#{account} or nick_name=#{account}")
    User getUser(String account);
}
