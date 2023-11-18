package webcurriculumdesign.backend.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import webcurriculumdesign.backend.cache.IGlobalCache;
import webcurriculumdesign.backend.data.constant.Constant;
import webcurriculumdesign.backend.data.dao.MessageDao;
import webcurriculumdesign.backend.data.dao.StaticValueDao;
import webcurriculumdesign.backend.data.constant.Role;
import webcurriculumdesign.backend.data.constant.TokenType;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.constant.MailTemplate;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.UserMapper;
import webcurriculumdesign.backend.util.CommonUtil;
import webcurriculumdesign.backend.util.JWTUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AuthService {
    @Resource(name = "cache")
    IGlobalCache iGlobalCache;
    @Resource
    private SpringTemplateEngine templateEngine;
    @Resource
    MailService mailService;
    @Resource
    StaticValueDao staticValue;
    @Resource
    MessageDao messageDao;
    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;

    // 注册
    @Transactional
    public Result signUp(String userMail, String password, String mailVerificationCode, String signUpRole) {
        // 校验邮件验证码
        String redisIKey = "MailVerificationCode-" + userMail;
        String trueCode = (String) iGlobalCache.get(redisIKey);
        if (trueCode == null || !trueCode.equals(mailVerificationCode)) return Result.error(Response.SC_UNAUTHORIZED, "验证码有误");

        // 密码加密并存储
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 判断用户权限
        User user = new User(null, userMail, hashedPassword, userMail, null, "");
        switch (signUpRole) {
            case "0" -> user.setRole(Role.ADMIN.role);
            case "1" -> user.setRole(Role.TEACHER.role);
            default -> user.setRole(Role.STUDENT.role);
        }

        // 插入用户
        userMapper.insert(user);
        Integer userId = user.getId();
        userService.insertInfo(userId, Integer.valueOf(signUpRole));

        return Result.ok();
    }

    // 获取邮件验证码
    public Result getMailVerificationCode(String userMail, String flag) {

        if (!userMail.contains("@")) return Result.error(Response.SC_BAD_REQUEST, "邮件格式错误");

        // 判断邮箱是否被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("mail", userMail);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null && !flag.equals("false")) return Result.error(Response.SC_BAD_REQUEST, "邮箱已被注册");

        // 生成邮件验证码
        String mailVerificationCode = CommonUtil.generateRandomString(Integer.parseInt(staticValue.getValue("mail_verification_code_length")));

        // 获取邮件提示信息并处理成map
        Map<String, String> messageDaoMap = new HashMap<>();
        List<Map<String, String>> messageDaoList = messageDao.getAllMessageContentAsListMap();
        for (Map<String, String> map : messageDaoList) {
            messageDaoMap.put(map.get("message_name"), map.get("message_content"));
        }

        // 解析并放入验证码
        Map<String, Object> variables = new HashMap<>();
        variables.put("mail_verification_code", mailVerificationCode);
        variables.put("prompt_message_for_code", messageDaoMap.get("prompt_message_for_code"));//获取邮件验证码提示信息
        variables.put("prompt_message_for_hint", messageDaoMap.get("prompt_message_for_signUp_hint"));//获取邮件提示信息
        variables.put("user_mail", userMail);
        Context context = new Context();
        context.setVariables(variables);
        String htmlWithCode = templateEngine.process("MailVerification", context);

        // 封装并异步发送邮件
        MailTemplate mailTemplate = new MailTemplate(userMail, messageDaoMap.get("mail_subject"), htmlWithCode, true);
        mailService.sendMail(mailTemplate);

        // 存入Redis
        String redisIKey = "MailVerificationCode-" + userMail;
        try {
            iGlobalCache.set(redisIKey, mailVerificationCode, Constant.MAIL_VERIFICATION_CODE_EXPIRE_TIME);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "Redis存储异常");
        }
        return Result.ok();
    }

    // 通过邮箱或昵称登录
    public Result login(String userMail, String password) {
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("mail", userMail);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) return Result.error(Response.SC_BAD_REQUEST, "用户不存在");

        // 判断密码是否正确
        if (!BCrypt.checkpw(password, user.getPassword())) return Result.error(Response.SC_UNAUTHORIZED, "密码错误");

        // 生成accessToken和refreshToken
        try{
            String refreshToken = JWTUtil.getTokenWithPayLoad(user.getId().toString(), user.getMail(), user.getNickName(), user.getRole(), Constant.REFRESH_EXPIRE_TIME, Constant.REFRESH_SECRET_KEY, TokenType.REFRESH.type);
            iGlobalCache.set(user.getMail(), refreshToken, Constant.REFRESH_EXPIRE_TIME);

            String accessToken = JWTUtil.getTokenWithPayLoad(user.getId().toString(), user.getMail(), user.getNickName(), user.getRole(), Constant.EXPIRE_TIME, Constant.SECRET_KEY, TokenType.ACCESS.type);

            Map<String, Object> map = new HashMap<>();
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            map.put("role", user.getRole());
            map.put("profile", user.getProfile());
            map.put("name", user.getNickName());
            map.put("mail", user.getMail());

            return Result.success(map);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
        }
    }

    // 通过邮箱更新用户密码
    public Result updatePassword(String verificationCode, String userMail, String newPassword) {
        String redisIKey = "MailVerificationCode-" + userMail;

        // 校验验证码
        if (iGlobalCache.get(redisIKey) == null || !iGlobalCache.get(redisIKey).equals(verificationCode)) {
            return Result.error(Response.SC_UNAUTHORIZED, "验证码错误");
        }

        // 更新密码
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("mail", userMail)
                .set("password", hashedPassword);

        try {
            userMapper.update(null, updateWrapper);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "密码更新失败");
        }

        return Result.success(null);
    }

    // 刷新token
    public Result refresh(String refreshToken) {
        try {
            // 获取refreshToken的payload信息
            DecodedJWT info = JWTUtil.getTokenInfo(refreshToken, Constant.REFRESH_SECRET_KEY);

            // 判断是否为refreshToken
            String tokenType = info.getClaim("type").asString();
            if (!tokenType.equals(TokenType.REFRESH.type)) return Result.error(Response.SC_UNAUTHORIZED, "验证错误");

            String id = info.getClaim("id").asString();
            String userName = info.getClaim("user_name").asString();
            String userMail = info.getClaim("user_mail").asString();
            String role = info.getClaim("role").asString();

            // 生成新token
            if (Objects.equals(refreshToken, iGlobalCache.get(userMail))) {
                String newAccessToken = JWTUtil.getTokenWithPayLoad(id, userMail, userName, role, Constant.EXPIRE_TIME, Constant.SECRET_KEY, TokenType.ACCESS.type);
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", newAccessToken);

                // 判断refreshToken时效
                long timeLeft = iGlobalCache.getExpire(userMail);
                if (timeLeft <= Constant.REFRESH_BOUND) {
                    String newRefreshToken = JWTUtil.getTokenWithPayLoad(id, userMail, userName, role, Constant.REFRESH_EXPIRE_TIME, Constant.REFRESH_SECRET_KEY, TokenType.REFRESH.type);
                    iGlobalCache.set(userMail, newRefreshToken, Constant.REFRESH_EXPIRE_TIME);
                    map.put("refreshToken", newRefreshToken);
                }

                return Result.success(map);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Result.error(Response.SC_BAD_REQUEST, "无有效RefreshToken");
    }
}
