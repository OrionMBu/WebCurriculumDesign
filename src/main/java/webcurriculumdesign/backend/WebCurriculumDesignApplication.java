package webcurriculumdesign.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = {"webcurriculumdesign.backend.mapper", "webcurriculumdesign.backend.data.dao"})
public class WebCurriculumDesignApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebCurriculumDesignApplication.class, args);
	}

}
