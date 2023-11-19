package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM info_user " +
            "LEFT JOIN info_student ON info_user.id = info_student.user_id " +
            "LEFT JOIN info_teacher ON info_user.id = info_teacher.user_id " +
            "LEFT JOIN info_admin ON info_user.id = info_admin.user_id " +
            "WHERE info_user.mail=#{mail}")
    User getUserByMail(String mail);
}
