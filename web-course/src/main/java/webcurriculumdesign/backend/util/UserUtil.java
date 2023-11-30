package webcurriculumdesign.backend.util;

import webcurriculumdesign.backend.data.constant.CurrentUser;

public class UserUtil {
    public static String getTableName() {
        switch (CurrentUser.role) {
            case "TEACHER" -> {
                return "course_teacher";
            }
            case "STUDENT" -> {
                return "course_student";
            }
            default -> throw new IllegalArgumentException("Invalid user role");
        }
    }
}
