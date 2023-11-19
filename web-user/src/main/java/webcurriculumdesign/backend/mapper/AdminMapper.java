package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.Admin;

public interface AdminMapper extends BaseMapper<Admin> {
    @Select("SELECT info_admin.*, info_academy.name AS academy FROM info_admin JOIN info_academy ON info_admin.academy_id = info_academy.id WHERE info_admin.user_id=#{userId}")
    Admin getAdminByUserId(String userId);
}
