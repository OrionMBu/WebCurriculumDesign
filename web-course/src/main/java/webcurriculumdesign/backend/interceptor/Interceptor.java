package webcurriculumdesign.backend.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.constant.Role;
import webcurriculumdesign.backend.data.constant.TokenType;
import webcurriculumdesign.backend.util.JWTUtil;

import javax.naming.AuthenticationException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 拦截器，判断用户权限，返回值为true则放行，false则中止
 */
@Component
public class Interceptor implements HandlerInterceptor {
    @Value("${secret_key.token}")
    private String SECRET_KEY;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 判断调用的是不是一个接口方法 TODO
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        // 获取请求头(Header)里"accessToken"内数据
        String accessToken = request.getHeader("accessToken");

        response.setContentType("application/json;charset=UTF-8");

        // java反射
        Method method = ((HandlerMethod) handler).getMethod();

        // 如果类上有该注解，则执行
        if(method.isAnnotationPresent(RequiredLogin.class)){
            // 如果注解中required = true则执行
            if(method.getAnnotation(RequiredLogin.class).required()){
                // 获取需求的权限
                String[] requiredRole = method.getAnnotation(RequiredLogin.class).roles();

                try{
                    // 验证token
                    JWTUtil.verify(accessToken, SECRET_KEY);

                    // 获取token的payload中信息
                    DecodedJWT info = JWTUtil.getTokenInfo(accessToken, SECRET_KEY);

                    // 判断是否为accessToken
                    String tokenType = info.getClaim("type").asString();
                    if (!tokenType.equals(TokenType.ACCESS.type)) return false;

                    // 获取用户权限
                    String userRole = info.getClaim("role").asString();

                    // 存储当前用户信息
                    CurrentUser.id = Integer.parseInt(info.getClaim("id").asString());
                    CurrentUser.userName = info.getClaim("user_name").asString();
                    CurrentUser.role = userRole;
                    CurrentUser.userMail = info.getClaim("user_mail").asString();

                    // 放行ADMIN
                    if (Objects.equals(userRole, Role.ADMIN.role)) return true;

                    // 权限需求为ALL则全放行
                    if (Objects.equals(requiredRole[0], "ALL")) return true;

                    // 进行权限比对
                    for (String role : requiredRole) {
                        if (Objects.equals(userRole, role)) return true;
                    }

                    throw new AuthenticationException();
                }catch (AuthenticationException e){
                    response.sendError(Response.SC_FORBIDDEN, "Permission Denied");
                    return false;
                } catch (Exception e){
                    response.sendError(Response.SC_UNAUTHORIZED, "Authentication failed");
                    return false;
                }
            }
        }
        return true;
    }
}
