package webcurriculumdesign.backend.test;

import com.alibaba.fastjson2.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.data.vo.Result;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/remote")
    public Result remote() {

        String[] list = {"{\"courseId\": \"sd02810587\", \"courseName\": \"人工智能基础\", \"courseLocation\": \"软件园1区203g\", \"courseWeeks\": [2, 4, 6, 8], \"courseOrder\": 2, \"courseTeacher\": \"刘九\", \"courseWeekday\": 1, \"userCourse\": false, \"courseType\": \"0\", \"credit\": 3}"
                , "{\"courseId\": \"sd02810586\", \"courseName\": \"网络编程实践\", \"courseLocation\": \"软件园2区302f\", \"courseWeeks\": [1, 3, 5, 7], \"courseOrder\": 3, \"courseTeacher\": \"张八\", \"courseWeekday\": 4, \"userCourse\": true, \"courseType\": \"2\", \"credit\": 2}"
                , "{\"courseId\": \"sd02810585\", \"courseName\": \"软件工程概论\", \"courseLocation\": \"软件园1区101e\", \"courseWeeks\": [1, 2, 3, 4, 5, 6, 7, 8], \"courseOrder\": 6, \"courseTeacher\": \"陈七\", \"courseWeekday\": 2, \"userCourse\": false, \"courseType\": \"0\", \"credit\": 3}"
                , "{\"courseId\": \"sd02810584\", \"courseName\": \"数据结构与算法\", \"courseLocation\": \"软件园1区202d\", \"courseWeeks\": [2, 4, 6, 8], \"courseOrder\": 1, \"courseTeacher\": \"赵六\", \"courseWeekday\": 5, \"userCourse\": true, \"courseType\": \"1\", \"credit\": 3}"
                , "{\"courseId\": \"sd02810583\", \"courseName\": \"操作系统原理\", \"courseLocation\": \"软件园2区205c\", \"courseWeeks\": [1, 3, 5, 7], \"courseOrder\": 3, \"courseTeacher\": \"王五\", \"courseWeekday\": 4, \"userCourse\": false, \"courseType\": \"0\", \"credit\": 4}"
                , "{\"courseId\": \"sd02810582\", \"courseName\": \"数据库设计与实现\", \"courseLocation\": \"软件园1区103b\", \"courseWeeks\": [2, 4, 6, 8], \"courseOrder\": 4, \"courseTeacher\": \"李四\", \"courseWeekday\": 2, \"userCourse\": true, \"courseType\": \"2\", \"credit\": 3}"
                , "{\"courseId\": \"sd02810581\", \"courseName\": \"计算机科学导论\", \"courseLocation\": \"软件园2区301a\", \"courseWeeks\": [1, 3, 5, 7], \"courseOrder\": 2, \"courseTeacher\": \"张三\", \"courseWeekday\": 1, \"userCourse\": true, \"courseType\": \"1\", \"credit\": 4}"
                , "{\"courseId\":\"sd02810580\",\"courseName\":\"习近平新时代中国特色社会主义思想概论\",\"courseLocation\":\"软件园1区201d\",\"courseWeeks\":[1,2,3,4,5,6,7,8],\"courseOrder\":5,\"courseTeacher\":\"林红\",\"courseWeekday\":3,\"userCourse\":false,\"courseType\":\"0\",\"credit\":0}"
                , "{\"courseId\": \"sd02810588\", \"courseName\": \"软件测试与质量管理\", \"courseLocation\": \"软件园2区204h\", \"courseWeeks\": [1, 2, 3, 4, 5, 6, 7, 8], \"courseOrder\": 4, \"courseTeacher\": \"周十\", \"courseWeekday\": 3, \"userCourse\": true, \"courseType\": \"1\", \"credit\": 3}"
                , "{\"courseId\": \"sd02810589\", \"courseName\": \"移动应用开发\", \"courseLocation\": \"软件园1区105i\", \"courseWeeks\": [1, 3, 5, 7], \"courseOrder\": 5, \"courseTeacher\": \"吴十一\", \"courseWeekday\": 5, \"userCourse\": false, \"courseType\": \"0\", \"credit\": 3}"
        };
        List<Course> courseList = new ArrayList<>();
        for (String a : list) {
            Course course = JSON.parseObject(a, Course.class);
            courseList.add(course);
        }

        return Result.success(courseList);
    }
}
