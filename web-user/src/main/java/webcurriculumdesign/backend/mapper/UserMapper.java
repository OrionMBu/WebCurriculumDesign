package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM info_user WHERE mail=#{account} OR nick_name=#{account}")
    User getUser(String account);

    @Select("SELECT * FROM info_user")
    List<User> getUserList();

    @Select("SELECT * FROM info_user WHERE role=#{role}")
    List<User> getUserListByRole(String role);
}
