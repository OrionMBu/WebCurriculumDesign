package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.po.Teacher;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.TeacherService;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    TeacherService teacherService;

    /**
     * 更新用户信息
     *
     * @param teacher 待更新信息
     */
    @RequiredLogin(roles = "TEACHER")
    @PostMapping("/updateTeacherInfo")
    public Result updateTeacherInfo(@RequestBody Teacher teacher) {
        return teacherService.updateTeacherInfo(teacher, CurrentUser.id);
    }
}
