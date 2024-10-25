package com.jeesite.modules.search.utils;

import org.elasticsearch.search.SearchHit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityMapper {

    public static <T> T mapToEntity(SearchHit hit, Class<T> clazz) {
        T entity;
        try {
            // 创建实例
            entity = clazz.getDeclaredConstructor().newInstance();
            
            // 获取文档的源数据
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            // 遍历字段并设置值
            for (Map.Entry<String, Object> entry : sourceAsMap.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();
                
                // 使用反射设置字段
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(entity, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to map SearchHit to entity", e);
        }
        return entity;
    }
}
