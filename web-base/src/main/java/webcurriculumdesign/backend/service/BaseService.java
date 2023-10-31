package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.dao.StaticValueDao;
import webcurriculumdesign.backend.data.enums.Role;
import webcurriculumdesign.backend.data.po.MainMenu;
import webcurriculumdesign.backend.data.pojo.Constant;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.exception.FileUploadException;
import webcurriculumdesign.backend.mapper.MainMenuMapper;
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
    StaticValueDao staticValueDao;

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

    /**
     * 上传文件到指定路径
     *
     * @param file 文件
     * @param folderName 文件夹名（服务名）
     * @param fileName 文件名
     * @param isBaseFile 是否为基础文件
     * @return 文件路径 PREFIX_URL/userMail或BaseFile/folderName/filename
     * @throws IOException IO异常
     * @throws FileUploadException 文件异常
     */
    public String uploadFile(MultipartFile file, String folderName, String fileName, boolean isBaseFile) throws IOException, FileUploadException {
        // 判断文件是否为空
        if (file.isEmpty()) {
            throw new FileUploadException("上传文件为空");
        }

        byte[] fileBytes;
        try {
            // 获取文件内容
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new FileUploadException("获取文件异常");
        }

        // 获取文件地址
        StringBuilder builder = new StringBuilder(staticValueDao.getValue("file_path"));
        int preLength = builder.length();

        // 以用户邮箱或固定基础地址（BaseFile）创建文件地址
        File parentFile;
        if (isBaseFile) {
            parentFile = new File(builder.append("/").append("BaseFile").toString());
        } else {
            parentFile = new File(builder.append("/").append(CurrentUser.userMail).toString());
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

        builder.replace(0, preLength, Constant.PREFIX_URL);

        return builder.toString();
    }


}
