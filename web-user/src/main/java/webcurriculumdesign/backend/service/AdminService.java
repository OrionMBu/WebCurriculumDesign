package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webcurriculumdesign.backend.data.po.Admin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.AdminMapper;
import webcurriculumdesign.backend.util.MapUtil;
import webcurriculumdesign.backend.util.RedisUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Resource
    UserService<Admin> userService;
    @Resource
    AdminMapper adminMapper;
    @Resource
    MapUtil<Admin> mapUtil;
    @Resource
    RedisUtil redisUtil;

    // 获取管理员信息
    public Admin getAdminInfoByUserId(String userId) {
        return adminMapper.getAdminByUserId(userId);
    }

    // 插入学生信息（初始化）
    @Transactional
    public void insertAdminInfo(Integer userId) {
        Admin admin = new Admin();
        admin.setUserId(userId);
        adminMapper.insert(admin);
    }

    // 更新管理员信息
    public void updateAdminInfo(Admin admin, Integer userId) {
        try {
            userService.updateUserInfo(admin, adminMapper, userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 从Map获取Admin
    public Admin getAdminFromMap(Map<String, Object> data) {
        return mapUtil.setValuesFromMap(data, Admin.class);
    }

    public Result getLoginData(int pastDay, boolean containToday) {
        LocalDate today = LocalDate.now();
        List<Map<String, Integer>> resultList = new ArrayList<>();
        for (int i = 0; i <= pastDay; i++) {
            if (!containToday && i == 0) continue;
            LocalDate previousDay = today.minusDays(i);
            String day = previousDay.format(DateTimeFormatter.ofPattern("MM-dd"));
            String redisKey = "WebDesign:Login:" + day;

            Object value = redisUtil.get(redisKey);
            Map<String, Integer> map = new HashMap<>();
            if (value != null) {
                map.put(day, Integer.valueOf((String) value));
            } else {
                map.put(day, 0);
            }

            resultList.add(map);
        }
        return Result.success(resultList);
    }
}
