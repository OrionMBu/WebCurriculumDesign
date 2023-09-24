package webcurriculumdesign.backend.util;

import webcurriculumdesign.backend.data.po.MainMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseUtil {
    //递归生成课表
    public static List<MainMenu> buildMenuTree(List<MainMenu> menuList, Integer parentId) {
        List<MainMenu> tree = new ArrayList<>();
        for (MainMenu menu : menuList) {
            if (Objects.equals(menu.getParentId(), parentId)) {
                List<MainMenu> children = buildMenuTree(menuList, menu.getId());
                if (!children.isEmpty()) {
                    menu.setChildren(children);
                }
                tree.add(menu);
            }
        }
        return tree;
    }
}
