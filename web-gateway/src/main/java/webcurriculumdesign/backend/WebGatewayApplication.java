package webcurriculumdesign.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WebGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebGatewayApplication.class, args);
    }

}
