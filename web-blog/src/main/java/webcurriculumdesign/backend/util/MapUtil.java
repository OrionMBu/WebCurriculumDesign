package webcurriculumdesign.backend.util;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MapUtil<T> {
    // 从Map中赋值给对象
    public T setValuesFromMap(Map<String, Object> map, Class<T> clazz) {
        T newEntity;
        try {
            newEntity = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(e.getMessage());
            return null;
        }

        Class<?> currentClass = clazz;
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                String fieldName = field.getName();
                Object value = map.get(fieldName);

                if (value != null) {
                    try {
                        field.setAccessible(true);
                        field.set(newEntity, value);
                    } catch (IllegalAccessException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return newEntity;
    }

    public static Map<String, Object> convertObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> result = new HashMap<>();

        // 获取对象的所有字段，包括父类的字段
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 设置字段可访问
                result.put(field.getName(), field.get(obj));
            }
            clazz = clazz.getSuperclass(); // 获取父类的 Class 对象
        }

        return result;
    }
}
