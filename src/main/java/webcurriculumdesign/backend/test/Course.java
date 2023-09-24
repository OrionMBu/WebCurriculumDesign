package webcurriculumdesign.backend.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private String courseId;
    private String courseName;
    private String courseLocation;
    private List<Integer> courseWeeks;
    private int courseOrder;
    private String courseTeacher;
    private int courseWeekday;
    private boolean userCourse;
    private int courseType;
    private double credit;
}
