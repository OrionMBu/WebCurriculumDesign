package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
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
    @Select("SELECT audit_record.id AS recordId, audit.name AS name, appli.mail AS applicant, review.mail AS reviewer, " +
            "CASE WHEN status = 0 THEN 0 WHEN status = 1 THEN 33 WHEN status = 2 THEN 67 WHEN status = 3 THEN 100 END AS status " +
            "FROM audit_record " +
            "JOIN audit ON audit_record.audit_id = audit.id " +
            "JOIN info_user appli ON applicant = appli.id " +
            "JOIN info_user review ON reviewer = review.id " +
            "WHERE applicant=#{applicantId}")
    List<Map<String, Object>> getAuditByApplicantId(Integer applicantId);

    /**
     * 通过审批记录id获取审批
     *
     * @param recordId 审批记录id
     */
    @Select("SELECT *, audit.name AS name, audit.prompt AS prompt FROM audit_record JOIN audit ON audit_record.audit_id = audit.id WHERE audit_record.id = #{recordId}")
    AuditRecord getAuditByRecordId(@Param("recordId") Integer recordId);

    /**
     * 修改审批事件状态
     *
     * @param auditId 审批id
     * @param status 状态
     */
    @Update("UPDATE audit_record SET status = #{status} WHERE id = #{auditId}")
    void updateStatusById(@Param("auditId") Integer auditId, @Param("status") Integer status);

    /**
     * 插入新审批
     *
     * @param auditId 审批类型
     * @param content 内容
     * @param applicantId 申请者id
     * @param reviewerId 审批人id
     * @param status 状态
     */
    @Insert("INSERT INTO audit_record(audit_id, content, applicant, reviewer, status) VALUES (#{auditId}, #{content}, #{applicantId}, #{reviewerId}, #{status})")
    void insertAudit(@Param("auditId") Integer auditId, @Param("content") String content, @Param("applicantId") Integer applicantId, @Param("reviewerId") Integer reviewerId, @Param("status") Integer status);

}
