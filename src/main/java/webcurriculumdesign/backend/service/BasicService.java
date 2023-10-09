package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.enums.Role;
import webcurriculumdesign.backend.data.po.MainMenu;
import webcurriculumdesign.backend.data.pojo.CurrentUser;
import webcurriculumdesign.backend.mapper.MainMenuMapper;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.util.CourseUtil;

import java.util.*;

@Service
public class BasicService {
    @Resource
    MainMenuMapper mainMenuMapper;

    //获取主目录
    public Result getMainMenu() {
        List<MainMenu> menuList = mainMenuMapper.selectMenuTree(CurrentUser.role);
        List<MainMenu> treeList = CourseUtil.buildMenuTree(menuList, 0);
        return Result.success(treeList);
    }

    //添加新目录
    public Result addMenu(String name, Integer parent_id, String role, String route) {
        //判断用户组（role）正确性
        if (!role.equals(Role.ADMIN.role) && !role.equals(Role.STUDENT.role) && !role.equals(Role.TEACHER.role)) {
            return Result.error(Response.SC_BAD_REQUEST, "用户组错误");
        }

        List<MainMenu> menuList = mainMenuMapper.selectMenuTree(role);

        //判断名称是否重复
        for (MainMenu list : menuList) {
            if (list.getName().equals(name) || list.getRoute().equals(route)) return Result.error(Response.SC_BAD_REQUEST, "名称重复");
        }

        try {
            mainMenuMapper.insert(new MainMenu(null, name, parent_id, role, route, null));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "服务器错误");
        }

        return Result.ok();
    }
}
