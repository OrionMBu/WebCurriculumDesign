package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.po.Admin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AdminMapper;

@Service
public class AdminService {
    @Resource
    AdminMapper adminMapper;

    // 获取管理员信息
    public Result getAdminInfoByUserId(String userId) {
        return Result.success(adminMapper.getAdminByUserId(userId));
    }

    // 插入学生信息（初始化）
    @Transactional
    public void insertAdminInfo(Integer userId) {
        Admin admin = new Admin();
        admin.setUserId(userId);
        adminMapper.insert(admin);
    }
}
