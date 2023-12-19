package webcurriculumdesign.backend.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseAudit {
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

    /*
    授课教师
     */
    @TableField(exist = false)
    private List<String> teacher;

    /*
    备注
     */
    private String comment;

    /*
    平时成绩权重
     */
    private double regularWeight;

    /*
    期末权重
     */
    private double finalWeight;

    /*
    申请id
     */
    private int auditId;
}