package webcurriculumdesign.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtil {
    // 验证token
    public static void verify(String token, String key) {
        JWT.require(Algorithm.HMAC256(key)).build().verify(token);
    }

    // 返回token内容
    public static DecodedJWT getTokenInfo(String token, String key) {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token);
    }
}
