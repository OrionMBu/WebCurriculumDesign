package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.mapper.UserMapper;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    // 邮箱或昵称获取用户
    public User getUser(String account) {
        return userMapper.getUser(account);
    }
}
