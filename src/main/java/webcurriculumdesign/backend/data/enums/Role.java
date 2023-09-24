package webcurriculumdesign.backend.data.enums;

import lombok.Getter;

/**
 * 用户身份权限
 */
@Getter
public class Role {
    public static String ADMIN = "ADMIN";
    public static String STUDENT = "STUDENT";
    public static String TEACHER = "TEACHER";
}
