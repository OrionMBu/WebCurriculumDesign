package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.po.Student;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    StudentService studentService;

    /**
     * 更新用户信息
     *
     * @param student 待更新信息
     */
    @RequiredLogin(roles = "STUDENT")
    @PostMapping("/updateStudentInfo")
    public Result updateStudentInfo(@RequestBody Student student) {
        return studentService.updateStudentInfo(student, CurrentUser.id);
    }
}
