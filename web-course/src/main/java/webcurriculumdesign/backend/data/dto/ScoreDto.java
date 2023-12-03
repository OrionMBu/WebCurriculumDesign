package webcurriculumdesign.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {
    /*
    课程id
     */
    int id;

    /*
    课序号
     */
    String index;

    /*
    课程名称
     */
    String name;

    /*
    学分
     */
    double credit;

    /*
    平时成绩
     */
    double regular;

    /*
    期末成绩
     */
    double finalScore;

    /*
    总成绩
     */
    double total;

    /*
    绩点
     */
    double point;

    /*
    排名
     */
    int ranking;
}
