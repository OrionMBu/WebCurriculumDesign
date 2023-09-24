package webcurriculumdesign.backend.util;

import webcurriculumdesign.backend.data.enums.Constant;

import java.util.Random;

public class CommonUtil {

    /**
     * 生成一定长度的随机字符串
     * @param length 目标生成长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        String characters = Constant.CHARACTER_SET;
        StringBuilder randomString = new StringBuilder();

        //随机组合
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
