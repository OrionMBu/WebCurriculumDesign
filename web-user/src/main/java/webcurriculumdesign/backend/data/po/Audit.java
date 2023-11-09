package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 审批事件
 */
@Data
@AllArgsConstructor
@TableName("basic_audit")
public class Audit {
    /*
    主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    事件名称
     */
    private String name;

    /*
    申请者
     */
    private Integer applicant;

    /*
    审批者
     */
    private Integer reviewer;

    /*
    审批状态（1为通过自动审核，2为通过教师审核，3为通过管理员审核）
     */
    private Integer status;
}
