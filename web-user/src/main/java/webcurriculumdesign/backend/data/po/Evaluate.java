package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
学生评价表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_evaluate")
public class Evaluate {
    /*
    自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    评价者
     */
    private Integer evaluator;

    /*
    评价者姓名
     */
    @TableField(exist = false)
    private String nameOfEvaluator;

    /*
    被评价者
     */
    private Integer evaluated;

    /*
    被评价者姓名
     */
    @TableField(exist = false)
    private String nameOfEvaluated;

    /*
    思想道德
     */
    private Integer moral;

    /*
    学习态度
     */
    private Integer attitude;

    /*
    实践能力
     */
    private Integer practice;

    /*
    总分
     */
    @TableField(exist = false)
    private Integer total;
}
