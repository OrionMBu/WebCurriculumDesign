package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新闻表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("basic_news")
public class News {
    /*
    主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    新闻标题
     */
    private String title;

    /*
    新闻正文
     */
    private String content;

    /*
    新闻地址
     */
    private String address;
}
