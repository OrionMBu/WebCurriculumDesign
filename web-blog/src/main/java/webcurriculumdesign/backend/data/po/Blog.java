package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog")
public class Blog {
    /*
    主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    用户id
     */
    private Integer userId;

    /*
    用户名
     */
    @TableField(exist = false)
    private String author;

    /*
    正文
     */
    private String title;

    /*
    摘要
     */
    private String digest;

    /*
    封面图片
     */
    private String image;

    /*
    正文
     */
    private String content;

    /*
    浏览次数
     */
    private Integer browse;

    /*
    点赞次数
     */
    private Integer like;
}
