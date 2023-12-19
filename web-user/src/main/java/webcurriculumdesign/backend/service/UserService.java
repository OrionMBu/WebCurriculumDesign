package webcurriculumdesign.backend.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.dto.FileToUpload;
import webcurriculumdesign.backend.data.constant.Role;
import webcurriculumdesign.backend.data.po.BaseInfo;
import webcurriculumdesign.backend.data.po.Evaluate;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AcademyMapper;
import webcurriculumdesign.backend.mapper.EvaluateMapper;
import webcurriculumdesign.backend.mapper.UserMapper;
import webcurriculumdesign.backend.util.MapUtil;

import java.io.IOException;
import java.util.*;

@Service
public class UserService<T extends BaseInfo> {
    @Resource
    UserMapper userMapper;
    @Resource
    AcademyMapper academyMapper;
    @Resource
    EvaluateMapper evaluateMapper;
    @Resource
    BaseService baseService;
    @Resource
    AdminService adminService;
    @Resource
    StudentService studentService;
    @Resource
    TeacherService teacherService;

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
                if (!CurrentUser.role.equals(Role.ADMIN.role)) {
                    return Result.success(userMapper.getUserListWithoutAdmin());
                } else {
                    return Result.success(userMapper.getUserList());
                }

            }
        }
    }

    // 获取用户信息
    public BaseInfo getUserInfo(String userId) {
        User user = userMapper.selectById(userId);
        return switch (user.getRole()) {
            case "TEACHER" -> teacherService.getTeacherInfoByUserId(userId);
            case "STUDENT" -> studentService.getStudentInfoByUserId(userId);
            case "ADMIN" -> {
                if (!CurrentUser.role.equals(Role.ADMIN.role)) {
                    yield  new BaseInfo();
                } else {
                    yield adminService.getAdminInfoByUserId(userId);
                }
            }
            default -> new BaseInfo();
        };
    }

    // 更新个人信息
    public void updateUserInfo(T user, BaseMapper<T> mapper, Integer userId) throws Exception {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);

        String academyName = user.getAcademy();
        if (academyName != null && !academyName.isEmpty()) {
            Integer academyId = academyMapper.getIdByName(academyName);
            if (academyId != null) {
                updateWrapper.set("academy_id", academyId);
            } else {
                throw new Exception("学院错误");
            }
        }
        mapper.update(user, updateWrapper);
    }

    // 更新指定用户信息
    public Result updateAppointedUser(Map<String, Object> data) {
        Integer userId = (Integer) data.get("id");
        data.remove("id");
        User user = userMapper.selectById(userId);
        try {
            switch (user.getRole()) {
                case "TEACHER" -> teacherService.updateTeacherInfo(teacherService.getTeacherFromMap(data), userId);
                case "STUDENT" -> studentService.updateStudentInfo(studentService.getStudentFromMap(data), userId);
                case "ADMIN" -> adminService.updateAdminInfo(adminService.getAdminFromMap(data), userId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "更新错误");
        }
        return Result.ok();
    }



    // 邮箱或昵称获取用户
    public User getUser(String account) {
        return userMapper.getUser(account);
    }

    // 通过用户id获取用户信息
    public Map<String, Object> getUserData(int userId) throws IllegalAccessException {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        BaseInfo userInfo = switch (user.getRole()) {
            case "TEACHER" -> teacherService.getTeacherInfoByUserId(String.valueOf(userId));
            case "STUDENT" -> studentService.getStudentInfoByUserId(String.valueOf(userId));
            case "ADMIN" -> adminService.getAdminInfoByUserId(String.valueOf(userId));
            default -> new BaseInfo();
        };

        // 处理数据
        Map<String, Object> userData = MapUtil.convertObjectToMap(userInfo);
        Map<String, Object> userBaseData = MapUtil.convertObjectToMap(userMapper.selectById(userId));
        userData.putAll(userBaseData);
        return userData;
    }

    // 插入信息（初始化）
    public void insertInfo(Integer userId, Integer role) {
        switch (role) {
            case 0 -> adminService.insertAdminInfo(userId);
            case 1 -> teacherService.insertTeacherInfo(userId);
            default -> studentService.insertStudentInfo(userId);
        }
    }


    // 获取评价信息
    public Result getToEvaluate() {
        return Result.success(evaluateMapper.getEvaluateByEvaluatorId(CurrentUser.id));
    }

    // 获取被评价信息
    public Result getEvaluated() {
        return Result.success(evaluateMapper.getEvaluateByEvaluatedId(CurrentUser.id));
    }

    // 评价
    public Result evaluate(int evaluateId, int moral, int attitude, int practice) {
        // 判断
        if (evaluateMapper.isToEvaluate(evaluateId, CurrentUser.id) != 1 && !CurrentUser.role.equals(Role.ADMIN.role)) return Result.error(Response.SC_FORBIDDEN, "不能做这个评价");

        // 判断是否更新
        UpdateWrapper<Evaluate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", evaluateId);
        if (moral >= 0 && moral <= 100) updateWrapper.set("moral" ,moral);
        if (attitude >= 0 && moral <= 100) updateWrapper.set("attitude", attitude);
        if (practice >= 0 && practice <= 100) updateWrapper.set("practice", practice);

        try {
            // 更新评价
            evaluateMapper.update(null, updateWrapper);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
        }
    }
}
