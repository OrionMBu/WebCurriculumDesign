package webcurriculumdesign.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import webcurriculumdesign.backend.interceptor.Interceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /*
    添加拦截器，进行token验证
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor())
                .addPathPatterns("/**");
    }
}
