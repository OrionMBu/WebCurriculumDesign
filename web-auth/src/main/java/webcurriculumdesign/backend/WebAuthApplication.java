package webcurriculumdesign.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "webcurriculumdesign.backend.service")
@SpringBootApplication
@MapperScan({"webcurriculumdesign.backend.mapper", "webcurriculumdesign.backend.data.dao"})
public class WebAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebAuthApplication.class, args);
	}

}
