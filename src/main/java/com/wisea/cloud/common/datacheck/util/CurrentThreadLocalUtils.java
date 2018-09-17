package com.wisea.cloud.common.datacheck.util;

import java.util.Map;

import org.springframework.core.NamedThreadLocal;

import com.google.common.collect.Maps;

/**
 * 线程变量保持工具类
 * 
 * @author XuDL(Wisea)
 *
 *         2018年3月6日 上午10:03:45
 */
public class CurrentThreadLocalUtils {
    private static final ThreadLocal<Map<String, Object>> currentThreadLocalHolder = new NamedThreadLocal<Map<String, Object>>("CurrentThreadLocal");

    public static void put(String key, Object value) {
        Map<String, Object> map = currentThreadLocalHolder.get();
        if (ConverterUtil.isEmpty(map)) {
            map = Maps.newHashMap();
        }
        map.put(key, value);
        currentThreadLocalHolder.set(map);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Map<String, Object> map = currentThreadLocalHolder.get();
        if (ConverterUtil.isEmpty(map)) {
            return null;
        }
        Object obj = map.get(key);
        if (ConverterUtil.isEmpty(obj)) {
            return null;
        }
        return (T) obj;
    }

    public static void remove(String key) {
        Map<String, Object> map = currentThreadLocalHolder.get();
        if (ConverterUtil.isNotEmpty(map)) {
            map.remove(key);
        }
    }

    public static void clear() {
        Map<String, Object> map = currentThreadLocalHolder.get();
        if (ConverterUtil.isNotEmpty(map)) {
            map.clear();
        }
    }
}
