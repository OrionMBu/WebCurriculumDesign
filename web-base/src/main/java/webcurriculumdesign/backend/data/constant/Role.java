package webcurriculumdesign.backend.data.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户身份权限
 */
@AllArgsConstructor
@Getter
public enum Role {

    ADMIN("ADMIN"),
    STUDENT("STUDENT"),
    TEACHER("TEACHER");

    public final String role;
}
