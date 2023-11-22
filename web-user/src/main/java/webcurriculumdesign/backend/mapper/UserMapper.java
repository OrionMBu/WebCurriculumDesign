package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.dto.UserInfoSimple;
import webcurriculumdesign.backend.data.po.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM info_user WHERE mail=#{account}")
    User getUser(String account);

    @Select("SELECT * FROM info_user")
    List<UserInfoSimple> getUserList();

    @Select("SELECT * FROM info_user WHERE role <> 'ADMIN'")
    List<UserInfoSimple> getUserListWithoutAdmin();

    @Select("SELECT * FROM info_user WHERE role=#{role}")
    List<UserInfoSimple> getUserListByRole(String role);
}
