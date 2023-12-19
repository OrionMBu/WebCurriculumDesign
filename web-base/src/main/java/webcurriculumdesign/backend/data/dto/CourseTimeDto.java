package webcurriculumdesign.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTimeDto {
    /*
    上课周
     */
    private List<Integer> week;

    /*
    上课星期
     */
    private int weekday;

    /*
    上课节次
     */
    private int order;
}
