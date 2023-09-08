package webcurriculumdesign.backend.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/remote")
    public String remote() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Yes!");
        }
        return "Success!";
    }
}
