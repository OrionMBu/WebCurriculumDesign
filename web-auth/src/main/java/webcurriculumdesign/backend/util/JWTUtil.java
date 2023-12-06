package webcurriculumdesign.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import webcurriculumdesign.backend.data.constant.TokenType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${secret_key.token}")
    public String SECRET_KEY;
    @Value("${secret_key.refresh_token}")
    public String REFRESH_SECRET_KEY;
    @Value("${expire_time.token}")
    public Integer EXPIRE_TIME;
    @Value("${expire_time.refresh_token}")
    public Integer REFRESH_EXPIRE_TIME;
    @Value("${expire_time.refresh_bound}")
    public Integer REFRESH_BOUND;

    //生成token
    public static String getToken(Map<String, String> map, int expireTime, String key) {
        //获取时间，设置token过期时间
        Instant instant = Instant.now();
        Instant newInstant = instant.plusSeconds(expireTime);

        //JWT添加payload
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);

        //JWT过期时间 + signature
        return builder.withExpiresAt(newInstant).sign(Algorithm.HMAC256(key));
    }

    //返回token内容
    public DecodedJWT getTokenInfo(String token, String type) {
        if (type.equals(TokenType.ACCESS.type)) {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
        } else {
            return JWT.require(Algorithm.HMAC256(REFRESH_SECRET_KEY)).build().verify(token);
        }
    }

    public String getTokenWithPayLoad(String id, String userMail, String role, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_mail", userMail);
        map.put("role", role);
        map.put("type", type);
        if (type.equals(TokenType.ACCESS.type)) {
            return JWTUtil.getToken(map, EXPIRE_TIME, SECRET_KEY);
        } else {
            return JWTUtil.getToken(map, REFRESH_EXPIRE_TIME, REFRESH_SECRET_KEY);
        }

    }
}
