package webcurriculumdesign.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    //生成token
    public static String getToken(Map<String, String> map, int expireTime, String key) {
        //获取时间，设置token过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, expireTime);

        //JWT添加payload
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);

        //JWT过期时间 + signature
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(key));
    }

    //返回token内容
    public static DecodedJWT getTokenInfo(String token, String key) {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token);
    }

    public static String getTokenWithPayLoad(String userMail, String userName, String role, int expireTime, String key, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("user_mail", userMail);
        map.put("user_name", userName);
        map.put("role", role);
        map.put("type", type);
        return JWTUtil.getToken(map, expireTime, key);
    }
}
