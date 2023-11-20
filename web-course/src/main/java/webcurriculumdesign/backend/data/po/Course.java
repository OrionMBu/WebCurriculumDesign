package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webcurriculumdesign.backend.data.dto.CourseTimeDto;

import java.util.List;

/**
 * 课程
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("course")
public class Course {
    /*
    自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    课程名称
     */
    private String name;

    /*
    课序号
     */
    private String index;

    /*
    学院
     */
    @TableField(exist = false)
    private String academy;

    /*
    上课地址
     */
    private String location;

    /*
    课程类型（0 -> 必修，1 -> 限选，2 -> 选修）
     */
    private int type;

    /*
    学分
     */
    private String credit;

    /*
    上课时间
     */
    @TableField(exist = false)
    private List<CourseTimeDto> courseTimeList;

//    /*
//    上课周
//     */
//    @TableField(exist = false)
//    private String week;
//
//    /*
//    上课星期
//     */
//    @TableField(exist = false)
//    private int weekday;
//
//    /*
//    上课节次
//     */
//    @TableField(exist = false)
//    private int order;

    /*
    授课教师
     */
    @TableField(exist = false)
    private List<String> teacher;
}
