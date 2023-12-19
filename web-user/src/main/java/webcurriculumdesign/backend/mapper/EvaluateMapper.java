package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.Evaluate;

import java.util.List;

@Mapper
public interface EvaluateMapper extends BaseMapper<Evaluate> {

    /**
     * 通过评价者id获取评价信息
     *
     * @param evaluatorId 评价者id
     */
    @Select("SELECT *, ((IF(moral != -1, moral, 0) + IF(attitude != -1, attitude, 0) + IF(practice != -1, practice, 0)) / 3) AS total, ifs1.name AS nameOfEvaluator, ifs2.name AS nameOfEvaluated " +
            "FROM user_evaluate " +
            "JOIN info_student ifs1 ON evaluator = ifs1.user_id " +
            "JOIN info_student ifs2 ON evaluated = ifs2.user_id " +
            "WHERE evaluator = #{evaluatorId}")
    List<Evaluate> getEvaluateByEvaluatorId(@Param("evaluatorId") Integer evaluatorId);

    /**
     * 通过被评价者id获取评价信息
     *
     * @param evaluatedId 被评价者id
     */
    @Select("SELECT * " +
            "FROM user_evaluate " +
            "WHERE evaluated = #{evaluatedId}")
    List<Evaluate> getEvaluateByEvaluatedId(@Param("evaluatedId") Integer evaluatedId);

    /**
     * 判断是否是评价者
     *
     * @param evaluateId 评价id
     * @param evaluatorId 评价者id
     */
    @Select("SELECT COUNT(*) FROM user_evaluate WHERE id = #{evaluateId} AND evaluator = #{evaluatorId} LIMIT 1")
    int isToEvaluate(@Param("evaluateId") Integer evaluateId, @Param("evaluatorId") Integer evaluatorId);
}
