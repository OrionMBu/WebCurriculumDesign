package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上课时间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("course_time")
public class CourseTime {
    /*
    自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    课程id
     */
    private Integer courseId;

    /*
    上课周
     */
    private String week;

    /*
    上课星期
     */
    private int weekday;

    /*
    上课节次
     */
    private int order;
}
