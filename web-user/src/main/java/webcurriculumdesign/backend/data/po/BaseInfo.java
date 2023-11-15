package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 基础信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseInfo {
    /*
    自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    用户id
     */
    private Integer userId;

    /*
    真实姓名
     */
    private String name;

    /*
    性别
     */
    private String gender;

    /*
    年龄
     */
    private int age;

    /*
    出生日期（YYYY-MM-dd）
     */
    private LocalDate birthday;

    /*
    政治面貌
     */
    private String politics;

    /*
    民族
     */
    private String nationality;

    /*
    学院
     */
    private String academy;

    /*
    手机号
     */
    private String phone;

    /*
    来源
     */
    private String origin;

    /*
    地址
     */
    private String address;

    /*
    个性签名
     */
    private String signature;

    /*
    自我介绍
     */
    private String introduction;
}
