package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.MainMenu;

import java.util.List;

@Mapper
public interface MainMenuMapper extends BaseMapper<MainMenu> {
    @Select("SELECT * FROM main_menu where role=#{role}")
    List<MainMenu> selectMenuTree(String role);
}
