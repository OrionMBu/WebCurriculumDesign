package webcurriculumdesign.backend.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.dto.FileToUpload;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.UserMapper;

import java.io.IOException;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    @Resource
    BaseService baseService;

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

        // 保存图片
        String userProfile;
        try {
            userProfile = baseService.uploadFile(new FileToUpload(fileBytes, CurrentUser.userMail, "Profile", file.getOriginalFilename(), false));
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
}
