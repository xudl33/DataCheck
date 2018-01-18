package com.wisea.cloud.common.datacheck.entity;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * DataCheck的logic模型
 * <p/>
 * 声明了@CheckMethod注解的class中所有的方法或某个方法，会被动态注册到@Check的logic的语法中。
 * 
 * @author XuDL(Wisea)
 *
 *         2017年12月25日 下午4:03:33
 */
public interface DataCheckModel {
    /**
     * 获取配置
     * 
     * @return
     */
    Map<String, Method> getConfig();
}
