package webcurriculumdesign.backend.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.dto.FileToUpload;
import webcurriculumdesign.backend.data.enums.Role;
import webcurriculumdesign.backend.data.po.Audit;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AuditMapper;
import webcurriculumdesign.backend.mapper.UserMapper;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    BaseService baseService;
    @Resource
    AdminService adminService;
    @Resource
    StudentService studentService;
    @Resource
    TeacherService teacherService;
    @Resource
    AuditMapper auditMapper;

    // 邮箱或昵称获取用户
    public User getUser(String account) {
        return userMapper.getUser(account);
    }

    // 修改头像
    public Result changeProfile(MultipartFile file) {
        // 判断是否为图片
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error(Response.SC_BAD_REQUEST, "请上传图片");
        }

        // 获取文件内容
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "文件获取异常");
        }

        // 获取上传文件的原始文件名
        String originalFilename = file.getOriginalFilename();

        // 从原始文件名中提取文件扩展名
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // 保存图片
        String userProfile;
        try {
            userProfile = baseService.uploadFile(new FileToUpload(fileBytes, CurrentUser.userMail, "Profile", "profile" + fileExtension, false));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "头像上传失败");
        }

        // 更新用户信息
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("profile", userProfile)
                .eq("mail", CurrentUser.userMail);
        try {
            userMapper.update(null, updateWrapper);
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "数据更新失败");
        }

        return Result.success(userProfile);
    }

    // 获取用户列表
    public Result getUserList(int type) {
        switch (type) {
            case 0 -> {
                if (!CurrentUser.role.equals(Role.ADMIN.role)) {
                    return Result.success(null);
                } else {
                    return Result.success(userMapper.getUserListByRole(Role.ADMIN.role));
                }
            }
            case 1 -> {
                return Result.success(userMapper.getUserListByRole(Role.TEACHER.role));
            }
            case 2 -> {
                return Result.success(userMapper.getUserListByRole(Role.STUDENT.role));
            }
            default -> {
                return Result.success(null);
            }
        }
    }

    // 获取用户信息
    public Result getUserInfo(String userId) {
        User user = userMapper.selectById(userId);
        return switch (user.getRole()) {
            case "TEACHER" -> teacherService.getTeacherInfoByUserId(userId);
            case "STUDENT" -> studentService.getStudentInfoByUserId(userId);
            case "ADMIN" -> {
                Result.success(null);
                if (!CurrentUser.role.equals(Role.ADMIN.role)) {
                    yield Result.success(null);
                } else {
                    yield adminService.getAdminInfoByUserId(userId);
                }
            }
            default -> Result.success(null);
        };
    }

    // 获取用户申请结果
    public Result getAuditResult() {
        // 查询结果
        List<Audit> auditList = auditMapper.getAuditByApplicantId(CurrentUser.id);
        List<Map<String, Object>> resultList = new ArrayList<>();

        // 处理
        for (Audit audit : auditList) {
            Map<String, Object> auditMap = new HashMap<>();
            auditMap.put("id", audit.getId());
            auditMap.put("name", audit.getName());
            auditMap.put("status", audit.getStatus());

            if (audit.getReviewer() != null) {
                User user = userMapper.selectById(audit.getReviewer());
                auditMap.put("reviewer", user.getMail());
            }

            resultList.add(auditMap);
        }

        return Result.success(resultList);
    }

    // 插入信息（初始化）
    public void insertInfo(Integer userId, Integer role) {
        switch (role) {
            case 0 -> adminService.insertAdminInfo(userId);
            case 1 -> teacherService.insertTeacherInfo(userId);
            default -> studentService.insertStudentInfo(userId);
        }
    }
}
