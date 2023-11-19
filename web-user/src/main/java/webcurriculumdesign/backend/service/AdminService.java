package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.po.Admin;
import webcurriculumdesign.backend.mapper.AdminMapper;
import webcurriculumdesign.backend.util.MapUtil;

import java.util.Map;

@Service
public class AdminService {
    @Resource
    UserService<Admin> userService;
    @Resource
    AdminMapper adminMapper;
    @Resource
    MapUtil<Admin> mapUtil;

    // 获取管理员信息
    public Admin getAdminInfoByUserId(String userId) {
        return adminMapper.getAdminByUserId(userId);
    }

    // 插入学生信息（初始化）
    @Transactional
    public void insertAdminInfo(Integer userId) {
        Admin admin = new Admin();
        admin.setUserId(userId);
        adminMapper.insert(admin);
    }

    // 更新管理员信息
    public void updateAdminInfo(Admin admin, Integer userId) {
        try {
            userService.updateUserInfo(admin, adminMapper, userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 从Map获取Admin
    public Admin getAdminFromMap(Map<String, Object> data) {
        return mapUtil.setValuesFromMap(data, Admin.class);
    }
}
