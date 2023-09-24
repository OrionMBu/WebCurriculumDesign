package webcurriculumdesign.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import webcurriculumdesign.backend.cache.IGlobalCache;
import webcurriculumdesign.backend.data.enums.Constant;
import webcurriculumdesign.backend.data.dao.MessageDao;
import webcurriculumdesign.backend.data.dao.StaticValueDao;
import webcurriculumdesign.backend.data.enums.Role;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.data.pojo.MailTemplate;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.UserMapper;
import webcurriculumdesign.backend.util.CommonUtil;
import webcurriculumdesign.backend.util.JWTUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    @Resource
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

    //注册
    public Result signUp(String userMail, String password, String mailVerificationCode) {
        //校验邮件验证码
        String redisIKey = "MailVerificationCode-" + userMail;
        String trueCode = (String) iGlobalCache.get(redisIKey);
        if (trueCode == null || !trueCode.equals(mailVerificationCode)) return Result.error(Response.SC_UNAUTHORIZED, "验证码有误");

        //密码加密并存储
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userMapper.insert(new User(null, userMail, hashedPassword, null, Role.STUDENT));

        return Result.ok();
    }

    //获取邮件验证码
    public Result getMailVerificationCode(String userMail, String flag) {

        if (!userMail.contains("@")) return Result.error(Response.SC_BAD_REQUEST, "邮件格式错误");

        //判断邮箱是否被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("user_mail", userMail);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null && !flag.equals("false")) return Result.error(Response.SC_BAD_REQUEST, "邮箱已被注册");

        //生成邮件验证码
        String mailVerificationCode = CommonUtil.generateRandomString(Integer.parseInt(staticValue.getValue("mail_verification_code_length")));

        //获取邮件提示信息并处理成map
        Map<String, String> messageDaoMap = new HashMap<>();
        List<Map<String, String>> messageDaoList = messageDao.getAllMessageContentAsListMap();
        for (Map<String, String> map : messageDaoList) {
            messageDaoMap.put(map.get("message_name"), map.get("message_content"));
        }

        //解析并放入验证码
        Map<String, Object> variables = new HashMap<>();
        variables.put("mail_verification_code", mailVerificationCode);
        variables.put("prompt_message_for_code", messageDaoMap.get("prompt_message_for_code"));//获取邮件验证码提示信息
        variables.put("prompt_message_for_hint", messageDaoMap.get("prompt_message_for_signUp_hint"));//获取邮件提示信息
        variables.put("user_mail", userMail);
        Context context = new Context();
        context.setVariables(variables);
        String htmlWithCode = templateEngine.process("MailVerification", context);

        //封装并异步发送邮件
        MailTemplate mailTemplate = new MailTemplate(userMail, messageDaoMap.get("mail_subject"), htmlWithCode, true);
        mailService.sendMail(mailTemplate);

        //存入Redis
        String redisIKey = "MailVerificationCode-" + userMail;
        try {
            iGlobalCache.set(redisIKey, mailVerificationCode, Constant.MAIL_VERIFICATION_CODE_EXPIRE_TIME);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "Redis存储异常");
        }
        return Result.ok();
    }

    //通过邮箱或昵称登录
    public Result login(String account, String password) {
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("user_mail", account).or().eq("user_name", account);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) return Result.error(Response.SC_BAD_REQUEST, "用户不存在");

        //判断密码是否正确
        if (!BCrypt.checkpw(password, user.getPassword())) return Result.error(Response.SC_UNAUTHORIZED, "密码错误");

        //生成accessToken和refreshToken
        try{
            String refreshToken = JWTUtil.getTokenWithPayLoad(user.getUserMail(), user.getUserName(), user.getRole(), Constant.REFRESH_EXPIRE_TIME, Constant.REFRESH_SECRET_KEY);
            iGlobalCache.set(user.getUserMail(), refreshToken, Constant.REFRESH_EXPIRE_TIME);

            String token = JWTUtil.getTokenWithPayLoad(user.getUserMail(), user.getUserName(), user.getRole(), Constant.EXPIRE_TIME, Constant.SECRET_KEY);

            Map<String, String> map = new HashMap<>();
            map.put("accessToken", token);
            map.put("refreshToken", refreshToken);

            return Result.success(map);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
        }
    }

    //通过邮箱更新用户密码
    public Result updatePassword(String verificationCode, String newPassword) {
        String userMail = CurrentUser.userMail;
        String redisIKey = "MailVerificationCode-" + userMail;

        //校验验证码
        if (iGlobalCache.get(redisIKey) == null || !iGlobalCache.get(redisIKey).equals(verificationCode)) {
            return Result.error(Response.SC_UNAUTHORIZED, "验证码错误");
        }

        //更新密码
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("user_mail", userMail)
                .set("password", hashedPassword);

        try {
            userMapper.update(null, updateWrapper);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "密码更新失败");
        }

        return Result.success(null);
    }
}
