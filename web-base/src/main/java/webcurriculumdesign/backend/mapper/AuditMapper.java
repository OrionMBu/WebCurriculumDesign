package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.AuditRecord;

import java.util.List;
import java.util.Map;

@Mapper
public interface AuditMapper extends BaseMapper<AuditRecord> {
    /**
     * 查询申请人申请状态
     *
     * @param applicantId 申请人id
     */
    @Select("SELECT audit_record.id AS id, audit.name AS name, appli.mail AS applicant, review.mail AS reviewer, " +
            "CASE WHEN status = 0 THEN 0 WHEN status = 1 THEN 33 WHEN status = 2 THEN 67 WHEN status = 3 THEN 100 END AS status " +
            "FROM audit_record " +
            "JOIN audit ON audit_record.audit_id = audit.id " +
            "JOIN info_user appli ON applicant = appli.id " +
            "JOIN info_user review ON reviewer = review.id " +
            "WHERE applicant=#{applicantId}")
    List<Map<String, Object>> getAuditByApplicantId(Integer applicantId);
}
