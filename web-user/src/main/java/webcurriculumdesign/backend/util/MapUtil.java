package webcurriculumdesign.backend.util;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
}
