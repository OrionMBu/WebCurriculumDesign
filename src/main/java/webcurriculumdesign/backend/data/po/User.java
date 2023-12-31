package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    /*
    主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    用户邮箱
     */
    private String mail;

    /*
    用户密码
     */
    private String password;

    /*
    用户昵称
     */
    private String name;

    /*
    用户权限
     */
    private String role;
}
