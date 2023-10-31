package webcurriculumdesign.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("webcurriculumdesign.backend.mapper")
public class WebUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebUserApplication.class, args);
    }
}
