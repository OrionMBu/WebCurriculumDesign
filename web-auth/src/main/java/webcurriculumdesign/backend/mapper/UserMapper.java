package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import webcurriculumdesign.backend.data.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过邮箱获取用户
     *
     * @param mail 邮箱
     */
    @Select("SELECT * FROM info_user " +
            "LEFT JOIN info_student ON info_user.id = info_student.user_id " +
            "LEFT JOIN info_teacher ON info_user.id = info_teacher.user_id " +
            "LEFT JOIN info_admin ON info_user.id = info_admin.user_id " +
            "WHERE info_user.mail=#{mail}")
    User getUserByMail(String mail);

    /**
     * 通过邮箱更新登录时间
     *
     * @param lastLogin 上次登录时间
     * @param mail 邮箱
     */
    @Update("UPDATE info_user SET last_login_time = #{lastLogin} WHERE mail = #{mail}")
    void updateLoginTime(@Param("lastLogin") long lastLogin, @Param("mail") String mail);
}
