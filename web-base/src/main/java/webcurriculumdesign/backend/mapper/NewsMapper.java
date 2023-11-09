package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.News;

import java.util.List;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
    @Select("SELECT * FROM basic_news ORDER BY id DESC LIMIT #{number}")
    List<News> getLatestNews(int number);
}
