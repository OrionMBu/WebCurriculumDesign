package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    CourseService courseService;

    /**
     * 查询课程
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param courseName 课程名称
     * @param index 课序号
     * @param teacherName 授课教师
     * @param type 课程类型（0 -> 必修，1 -> 限选，2 -> 选修，3 -> 全部）
     */
    @GetMapping("/getCourseList")
    public Result getCourseList(@RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "20") int pageSize,
                                @RequestParam(required = false, defaultValue = "") String courseName,
                                @RequestParam(required = false, defaultValue = "") String index,
                                @RequestParam(required = false, defaultValue = "") String teacherName,
                                @RequestParam(required = false, defaultValue = "3") int type) {
        return courseService.getCourseList(page, pageSize, courseName, index, teacherName, type);
    }
}
