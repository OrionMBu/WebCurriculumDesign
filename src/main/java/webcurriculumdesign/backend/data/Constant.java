package webcurriculumdesign.backend.data;

/**
 * 常量表
 */
public class Constant {
    //密钥
    public static final String SECRET_KEY = "vDK6uz7)A_TjK([+ej&75zLTG7wdag@#";//accessToken密钥
    public static final String REFRESH_SECRET_KEY = "YunC8M￥rJP@8R6G*(W|VD#d1sH38h8Dl";//refreshToken密钥

    //过期时间
    public static final int EXPIRE_TIME = 5 * 24 * 3600;//Token过期时间
    public static final int REFRESH_EXPIRE_TIME = 30 * 24 * 3600;//RefreshToken过期时间
    public static final int REFRESH_POINT = 5 * 24 * 3600;//RefreshToken刷新界限
    public static final int MAIL_VERIFICATION_CODE_EXPIRE_TIME = 5 * 60;//邮箱验证码过期时间

    //用户权限
    public static final String SYS_ADMIN = "SYS_ADMIN";//管理员权限
    public static final String STUDENT = "STUDENT";//学生
    public static final String TEACHER = "TEACHER";//教师

    //返回状态码
    public static final Integer SUCCESS = 200;//请求成功
    public static final Integer UNKNOWN_ERROR = 500;//内部错误
    public static final Integer REQUEST_FAILED = 400;//请求失败
    public static final Integer AUTHENTICATED_FAILED = 401;//认证失败
    public static final Integer PERMISSION_DENIED = 403;//无权限

    //随机字符集
    public static final String CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//随机字符集
}
