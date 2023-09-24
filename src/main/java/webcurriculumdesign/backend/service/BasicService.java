package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
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
        List<MainMenu> treeList = CourseUtil.buildMenuTree(menuList, null);
        return Result.success(treeList);
    }
}
