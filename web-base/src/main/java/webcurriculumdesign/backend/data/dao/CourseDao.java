package webcurriculumdesign.backend.data.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface CourseDao {
    /**
     * 获取最大id
     *
     * @return 最大id
     */
    @Select("SELECT MAX(id) FROM course")
    Integer getMaxCourseId();

    /**
     * 插入新课程
     *
     * @param name 课程名称
     * @param location 上课地点
     * @param credit 绩点
     * @param academy 学院
     * @param finalWeight 期末权重
     */
    @Insert("INSERT INTO course(name, location, type, credit, academy_id,comment, regular_weight, final_weight) " +
            "VALUES (#{name}, #{location}, #{type}, #{credit}, " +
            "(SELECT info_academy.id FROM info_academy WHERE info_academy.name = #{academy}), #{comment}, 1 - #{finalWeight}, #{finalWeight})")
    void insertCourse(@Param("name") String name, @Param("location") String location, @Param("type") int type,
                      @Param("credit") String credit, @Param("academy") String academy,@Param("comment") String comment, @Param("finalWeight") double finalWeight);

    /**
     * 插入课程时间
     *
     * @param courseId 课序号
     * @param week 上课周
     * @param weekday 上课星期
     * @param order 上课节次
     */
    @Insert("INSERT INTO course_time(course_id, week, weekday, `order`) VALUES (#{courseId}, #{week}, #{weekday}, #{order})")
    void insertCourseTime(@Param("courseId") Integer courseId, @Param("week") String week, @Param("weekday") int weekday, @Param("order") int order);

    /**
     * 更新课序号
     *
     */
    @Update("UPDATE course SET `index` = CONCAT('WCD', LPAD(id, 5, '0')) WHERE id = #{id}")
    void updateIndex(@Param("id") Integer id);

}
