package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 学生
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("info_student")
public class Student extends BaseInfo{

    /*
    专业
     */
    private String major;

    /*
    学习层次（普通本科/硕士）
     */
    private String degree;

    /*
    学制
     */
    private String length;

    /*
    班级
     */
    private String classNumber;

    /*
    学号
     */
    private String number;

    /*
    特长
     */
    private String specialty;
}
