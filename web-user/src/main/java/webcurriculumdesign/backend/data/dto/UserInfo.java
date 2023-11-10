package webcurriculumdesign.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回用户类型封装
 */
@Data
@AllArgsConstructor
public class UserInfo {
    private Integer id;
    private String mail;
    private String nickName;
    private String role;
    private String profile;
}
