package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import webcurriculumdesign.backend.data.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
