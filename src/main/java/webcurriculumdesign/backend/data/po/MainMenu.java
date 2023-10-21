package webcurriculumdesign.backend.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 主目录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("main_menu")
public class MainMenu {
    /*
    主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    用户姓名
     */
    private String name;

    /*
    父节点id
     */
    private Integer parentId;

    /*
    用户权限
     */
    private String role;

    /*
    路径名称
     */
    private String route;

    /*
    图标
     */
    private String icon;

    /*
    子节点（不存储）
     */
    @TableField(exist = false)
    private List<MainMenu> children;
}
