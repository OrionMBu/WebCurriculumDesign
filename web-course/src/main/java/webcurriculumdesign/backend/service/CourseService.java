package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.constant.Role;
import webcurriculumdesign.backend.data.dao.CourseTeacherDao;
import webcurriculumdesign.backend.data.po.Course;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.CourseMapper;
import webcurriculumdesign.backend.util.CourseUtil;
import webcurriculumdesign.backend.util.UserUtil;

import java.util.*;

@Service
public class CourseService {
    @Resource
    UserService userService;
    @Resource
    CourseMapper courseMapper;
    @Resource
    CourseTeacherDao courseTeacherDao;
    @Resource
    CourseUtil courseUtil;

    // 查询课表
    public Result getCourseList() {
        List<Course> courseList;
        switch (CurrentUser.role) {
            case "TEACHER" -> {
                courseList = courseMapper.getTeacherCourse(CurrentUser.id);
                courseUtil.fillCourseData(courseList);
            }
            case "STUDENT" -> {
                courseList = courseMapper.getStudentCourse(CurrentUser.id);
                courseUtil.fillCourseData(courseList);
            }
            default -> {
                return Result.ok();
            }
        }
        return Result.success(courseList);
    }

    // 更新课程备注
    public Result updateComment(int id, String comment) {
        if (!CurrentUser.role.equals(Role.ADMIN.role)) {
            // 查询课程-教师记录
            int record = courseTeacherDao.getCourseTeacherRecord(CurrentUser.id, id);
            if (record == 0) return Result.error(Response.SC_FORBIDDEN, "只能修改自己授课的备注");
        }

        courseMapper.updateComment(id, comment);
        return Result.ok();
    }

    // 查询课程
    public Result searchCourse(int page, int pageSize, String courseName, String index, String teacherName, int type) {
        int offset = (page - 1) * pageSize;
        String typeChange = type == 3 ? "%" : String.valueOf(type);

        // 查询课程列表
        List<Course> courseList;
        if (!teacherName.isEmpty()) {
            courseList = courseMapper.searchCourseWithTeacherName("%" + courseName + "%", "%" + index + "%", "%" + teacherName + "%", typeChange, new RowBounds(offset, pageSize));
        } else {
            courseList = courseMapper.searchCourseWithoutTeacher("%" + courseName + "%", "%" + index + "%", typeChange, new RowBounds(offset, pageSize));
        }

        // 填充课程上课时间和教师信息
        courseUtil.fillCourseData(courseList);

        return Result.success(courseList);
    }

    // 选/退课（1 -> 选课，2 -> 退课）
    public Result courseSelection(int courseId, int userId, int mode) {
        // 管理员操作
        if (userId != 0 && CurrentUser.role.equals(Role.ADMIN.role)) {
            Map<String, Object> userData;
            try {
                userData = userService.getUserData(userId);
            } catch (Exception e) {
                return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
            }

            CurrentUser.role = (String) userData.get("role");
            CurrentUser.id = (Integer) userData.get("id");
        }

        try {
            // 获取表名
            String tableName = UserUtil.getTableName();

            // 插入/删除数据
            switch (mode) {
                case 1 -> courseMapper.insertCourseBinding(tableName, courseId, CurrentUser.id);
                case 2 -> courseMapper.deleteCourseBinding(tableName, courseId, CurrentUser.id);
            }
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 获取学生课程分数
    public Result getScore(Integer userId, Integer courseId) {
        // 指定用户模式
        if (userId != 0) {
            // 判断是否为管理员
            if (!CurrentUser.role.equals(Role.ADMIN.role)) return Result.error(Response.SC_UNAUTHORIZED, "无权限");
            else CurrentUser.id = userId;
        }

        // 结果列表
        Map<String, Object> resultMap = new HashMap<>();

        // 判断是否查询指定课程
        if (courseId != 0) {
            resultMap.put("score", courseMapper.getScoreByCourseId(CurrentUser.id, courseId));
        } else {
            resultMap.put("score", courseMapper.getScoreListByUserId(CurrentUser.id));
        }

        // 获取学生GPA和GPA排名信息
        resultMap.put("GPA", courseMapper.getStudentGPA(CurrentUser.id));

        return Result.success(resultMap);
    }

    // 更新学生课程成绩
    public Result updateScore(Integer userId, Integer courseId, Double regular, Double finalScore) {
        // 输入判断，成绩必须>=0，<=100，且不能都为-1
        if ((regular != -1 && (regular > 100 || regular < 0)) || (finalScore != -1 && (finalScore > 100 || finalScore < 0)) || (regular == -1 && finalScore == -1)) return Result.error(Response.SC_BAD_REQUEST, "数据错误");

        // 输入数据四舍五入保留一位小数
        regular = Math.round(regular * 10.0) / 10.0;
        finalScore = Math.round(finalScore * 10.0) / 10.0;

        // 修改成绩
        try {
            courseMapper.updateStudentScore(userId, courseId, regular, finalScore);
            courseUtil.updateStudentGPA(userId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
