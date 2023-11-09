package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.Audit;

import java.util.List;

@Mapper
public interface AuditMapper extends BaseMapper<Audit> {
    @Select("SELECT * FROM basic_audit WHERE applicant=#{applicantId}")
    List<Audit> getAuditByApplicantId(Integer applicantId);
}
