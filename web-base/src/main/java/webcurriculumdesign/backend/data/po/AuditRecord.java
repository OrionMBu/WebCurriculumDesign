package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 审批事件记录
 */
@Data
@AllArgsConstructor
@TableName("audit_record")
public class AuditRecord {
    /*
    主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    事件名称
     */
    @TableField(exist = false)
    private String name;

    /*
    事件内容
     */
    private String content;

    /*
    申请者
     */
    @TableField(exist = false)
    private String applicant;

    /*
    审批者
     */
    @TableField(exist = false)
    private String reviewer;

    /*
    审批状态（1为通过自动审核，2为通过教师审核，3为通过管理员审核）
     */
    private Integer status;

    /*
    行为提示符
     */
    @TableField(exist = false)
    private Integer prompt;
}
