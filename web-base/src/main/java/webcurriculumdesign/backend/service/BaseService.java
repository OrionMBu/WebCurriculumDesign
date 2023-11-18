package webcurriculumdesign.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.cache.IGlobalCache;
import webcurriculumdesign.backend.data.dao.StaticValueDao;
import webcurriculumdesign.backend.data.constant.Role;
import webcurriculumdesign.backend.data.po.MainMenu;
import webcurriculumdesign.backend.data.po.News;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.constant.Constant;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.exception.FileUploadException;
import webcurriculumdesign.backend.mapper.MainMenuMapper;
import webcurriculumdesign.backend.mapper.NewsMapper;
import webcurriculumdesign.backend.mapper.UserMapper;
import webcurriculumdesign.backend.util.MenuUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class BaseService {
    @Resource
    MainMenuMapper mainMenuMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    NewsMapper newsMapper;
    @Resource
    StaticValueDao staticValueDao;
    @Resource
    UserService userService;
    @Resource
    IGlobalCache iGlobalCache;

    // 更新用户密码
    public Result updatePassword(String previousPassword, String newPassword) {
        // 获取用户
        User user = userService.getUser(CurrentUser.userMail);
        if (user == null) return Result.error(Response.SC_BAD_REQUEST, "用户不存在");

        // 判断密码是否正确
        if (!BCrypt.checkpw(previousPassword, user.getPassword())) return Result.error(Response.SC_UNAUTHORIZED, "密码错误");

        // 更新密码
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("password", hashedPassword)
                .eq("mail", user.getMail());
        try {
            userMapper.update(null, updateWrapper);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "密码更新失败");
        }

        return Result.success(null);
    }

    public Result updateMail(String newMail, String mailVerificationCode) {
        // 判断邮箱是否被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("mail", newMail);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) return Result.error(Response.SC_BAD_REQUEST, "邮箱已被注册");

        // 校验邮件验证码
        String redisIKey = "MailVerificationCode-" + newMail;
        String trueCode = (String) iGlobalCache.get(redisIKey);
        if (trueCode == null || !trueCode.equals(mailVerificationCode)) return Result.error(Response.SC_UNAUTHORIZED, "验证码有误");

        // 更新邮箱
        User newUser = new User();
        newUser.setMail(newMail);
        try {
            userMapper.update(newUser, null);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 获取主目录
    public Result getMainMenu() {
        List<MainMenu> menuList = mainMenuMapper.selectMenuTree(CurrentUser.role);
        List<MainMenu> treeList = MenuUtil.buildMenuTree(menuList, 0);
        return Result.success(treeList);
    }

    // 添加新目录
    public Result addMenu(String name, Integer parent_id, String role, String route) {
        // 判断用户组（role）正确性
        if (!role.equals(Role.ADMIN.role) && !role.equals(Role.STUDENT.role) && !role.equals(Role.TEACHER.role)) {
            return Result.error(Response.SC_BAD_REQUEST, "用户组错误");
        }

        List<MainMenu> menuList = mainMenuMapper.selectMenuTree(role);

        // 判断名称是否重复
        for (MainMenu list : menuList) {
            if (list.getName().equals(name) || list.getRoute().equals(route)) return Result.error(Response.SC_BAD_REQUEST, "名称重复");
        }

        try {
            mainMenuMapper.insert(new MainMenu(null, name, parent_id, role, route, null, null));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "服务器错误");
        }

        return Result.ok();
    }

    // 获取最新新闻
    public Result getLatestNews(int number) {
        List<News> latestNewsList = newsMapper.getLatestNews(number);
        return Result.success(latestNewsList);
    }

    /**
     * 上传文件到指定路径
     *
     * @param fileBytes 文件
     * @param folderName 文件夹名（服务名）
     * @param fileName 文件名
     * @param isBaseFile 是否为基础文件
     * @return 文件路径 PREFIX_URL/userMail或BaseFile/folderName/filename
     * @throws IOException IO异常
     * @throws FileUploadException 文件异常
     */
    public String uploadFile(byte[] fileBytes, String userMail,String folderName, String fileName, boolean isBaseFile) throws IOException, FileUploadException {
        // 判断文件是否为空
        if (fileBytes.length == 0) {
            throw new FileUploadException("上传文件为空");
        }

        // 获取文件地址
        StringBuilder builder = new StringBuilder(staticValueDao.getValue("file_path"));
        int preLength = builder.length();

        // 以用户邮箱或固定基础地址（BaseFile）创建文件地址
        File parentFile;
        if (isBaseFile) {
            parentFile = new File(builder.append("/").append("BaseFile").toString());
        } else {
            parentFile = new File(builder.append("/").append(userMail).toString());
        }
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new FileUploadException("父文件夹创建失败");

        // 创建文件夹
        File folder = new File(builder.append("/").append(folderName).toString());
        if (!folder.exists() && !folder.mkdirs())
            throw new FileUploadException("子文件夹创建失败");

        // 判断文件是否存在
        File specificFile = new File(builder.append("/").append(fileName).toString());

        // 将文件保存到指定目录
        Files.write(Paths.get(specificFile.getPath()), fileBytes);

        // 生成文件路径
        builder.replace(0, preLength, Constant.PREFIX_URL);

        return builder.toString();
    }


}
