package com.wisea.cloud.common.datacheck;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;
import com.wisea.cloud.common.datacheck.annotation.CheckApi;
import com.wisea.cloud.common.datacheck.annotation.CheckModel;
import com.wisea.cloud.common.datacheck.entity.DataCheckModel;
import com.wisea.cloud.common.datacheck.util.ConverterUtil;

/**
 * 声明了@CheckMethod注解的class中所有的方法或某个方法，会被动态注册到@Check的logic的语法中。
 * 
 * @author XuDL(Wisea)
 *
 *         2017年12月25日 下午2:54:11
 */
@Configuration
public class CheckMethodConfiguration implements ApplicationContextAware {
    /**
     * 日志对象
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public DataCheckModel init() {
        return () -> {
            Map<String, Method> result = Maps.newHashMap();
            // 遍历所有的CheckModel
            Map<String, Object> extMap = applicationContext.getBeansWithAnnotation(CheckModel.class);
            extMap.forEach((k, v) -> {
                Method[] methodArray = v.getClass().getMethods();
                String key = "";
                for (Method method : methodArray) {
                    // 必须声明CheckApi注解
                    CheckApi api = method.getAnnotation(CheckApi.class);
                    if (ConverterUtil.isNotEmpty(api)) {
                        // 如果设置了name属性就用name
                        key = api.name();
                        // 没有就用method名
                        if (ConverterUtil.isEmpty(key)) {
                            key = method.getName();
                        }
                        // 若发现有重名的方法，则提示警告，不报错
                        if (result.containsKey(key)) {
                            logger.warn("found same check method with:[" + key + "],the old methods are replaced. You can use @CheckApi(name=\"newName\") to make a new check method'name.");
                        }
                        result.put(key, method);
                    }
                }
            });
            return result;
        };
    }

}
