package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.dto.FileToUpload;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.exception.FileUploadException;
import webcurriculumdesign.backend.service.BaseService;

import java.io.IOException;

@RestController
@RequestMapping("/base")
public class BaseController {
    @Resource
    BaseService baseService;

    /**
     * 更新用户密码（需要登录）
     *
     * @param previousPassword 旧密码
     * @param newPassword 新密码
     */
    @RequiredLogin(roles = "ALL")
    @PatchMapping("/updatePassword")
    public Result updatePassword(@RequestParam String previousPassword, @RequestParam String newPassword) {
        return baseService.updatePassword(previousPassword, newPassword);
    }

    /**
     * 更新用户邮箱
     *
     * @param newMail 新邮箱
     * @param mailVerificationCode 邮箱验证码
     */
    @RequiredLogin(roles = "ALL")
    @PatchMapping("/updateMail")
    public Result updateMail(@RequestParam String newMail, @RequestParam String mailVerificationCode) {
        return baseService.updateMail(newMail, mailVerificationCode);
    }

    /**
     * 获取主目录
     *
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getMainMenu")
    public Result getMainMenu() {
        return baseService.getMainMenu();
    }

    /**
     * 添加新的目录
     *
     * @param name 目录名称
     * @param role 用户组
     * @param route 目录路径
     * @param parent_id 父节点
     */
    @PostMapping("/addMenu")
    public Result addMenu(@RequestParam String name, @RequestParam Integer parent_id, @RequestParam String role, @RequestParam String route) {
        return baseService.addMenu(name, parent_id, role, route);
    }

    /**
     * 删除主目录
     *
     * @param menuId 目录id
     */
    @RequiredLogin
    @DeleteMapping("/deleteMenu")
    public Result deleteMenu(@RequestParam int menuId) {
        return baseService.deleteMenu(menuId);
    }

    /**
     * 获取学院列表
     *
     */
    @GetMapping("/getAcademyList")
    public Result getAcademyList() {
        return baseService.getAcademyList();
    }

    /**
     * 添加新学院
     *
     * @param academy 学院
     */
    @RequiredLogin
    @PostMapping("/addAcademy")
    public Result addAcademy(@RequestParam String academy) {
        return baseService.addAcademy(academy);
    }

    /**
     * 删除学院
     *
     * @param academyId 学院id
     */
    @RequiredLogin
    @DeleteMapping("/deleteAcademy")
    public Result deleteAcademy(@RequestParam int academyId) {
        return baseService.deleteAcademy(academyId);
    }

    /**
     * 获取最新新闻
     *
     * @param number 新闻数量
     */
    @GetMapping("/getNews")
    public Result getNews(@RequestParam(required = false, defaultValue = "3") int number) {
        return baseService.getLatestNews(number);
    }

    //----------------------内部微服务接口----------------------//

    /**
     * 上传文件
     *
     * @param fileToUpload 文件信息
     * @return 文件路径
     */
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestBody FileToUpload fileToUpload) throws IOException, FileUploadException {
        return baseService.uploadFile(fileToUpload.getFileBytes(), fileToUpload.getUserMail(), fileToUpload.getFolderName(), fileToUpload.getFileName(), fileToUpload.isBaseFile());
    }
}

