package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    CourseService courseService;

    /**
     * 查询课表
     *
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getCourseList")
    public Result getCourseList() {
        return courseService.getCourseList();
    }

    /**
     * 更新课程备注
     *
     * @param id 课程id
     * @param comment 备注
     */
    @RequiredLogin(roles = "TEACHER")
    @PatchMapping("/updateComment")
    public Result updateComment(@RequestParam int id, @RequestParam String comment) {
        return courseService.updateComment(id, comment);
    }

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
    @GetMapping("/searchCourse")
    public Result searchCourse(@RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "20") int pageSize,
                                @RequestParam(required = false, defaultValue = "") String courseName,
                                @RequestParam(required = false, defaultValue = "") String index,
                                @RequestParam(required = false, defaultValue = "") String teacherName,
                                @RequestParam(required = false, defaultValue = "3") int type) {
        return courseService.searchCourse(page, pageSize, courseName, index, teacherName, type);
    }

    /**
     * 选课（教师学生通用）
     *
     * @param courseId 课程id
     * @param userId 用户id（可选，需要管理员权限）
     */
    @RequiredLogin(roles = "ALL")
    @PostMapping("/selectCourse")
    public Result selectCourse(@RequestParam int courseId, @RequestParam(required = false, defaultValue = "0") int userId) {
        return courseService.courseSelection(courseId, userId, 1);
    }

    /**
     * 退课（教师学生通用）
     *
     * @param courseId 课程id
     * @param userId 用户id（可选，需要管理员权限）
     */
    @RequiredLogin(roles = "ALL")
    @PatchMapping("/dropCourse")
    public Result dropCourse(@RequestParam int courseId, @RequestParam(required = false, defaultValue = "0") int userId) {
        return courseService.courseSelection(courseId, userId, 2);
    }

    /**
     * 查询学生课程成绩信息
     *
     * @param userId 用户id（可选，需要管理员权限）
     * @param courseId 课程id（可选，带此参数则查询指定课程）
     */
    @RequiredLogin(roles = "STUDENT")
    @GetMapping("/getScore")
    public Result getScore(@RequestParam(required = false, defaultValue = "0") Integer userId, @RequestParam(required = false, defaultValue = "0") Integer courseId) {
        return courseService.getScore(userId, courseId);
    }

    /**
     * 更新学生课程成绩，同时异步更新GPA
     *
     * @param userId 用户id
     * @param courseId 课程id
     * @param regular 平时成绩
     * @param finalScore 期末成绩
     */
    @RequiredLogin(roles = "TEACHER")
    @PatchMapping("/updateScore")
    public Result updateScore(@RequestParam Integer userId,
                              @RequestParam Integer courseId,
                              @RequestParam(required = false, defaultValue = "-1") Double regular,
                              @RequestParam(required = false, defaultValue = "-1") Double finalScore) {
        return courseService.updateScore(userId, courseId, regular, finalScore);
    }
}
