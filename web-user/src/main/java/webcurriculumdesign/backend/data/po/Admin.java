package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("info_admin")
public class Admin extends BaseInfo{
}
