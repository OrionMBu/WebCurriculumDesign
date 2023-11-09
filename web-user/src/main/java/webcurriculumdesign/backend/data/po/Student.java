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
    年级
     */
    private int grade;

    /*
    班级
     */
    private int classNumber;
}
