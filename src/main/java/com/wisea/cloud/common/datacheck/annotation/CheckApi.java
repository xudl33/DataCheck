package com.wisea.cloud.common.datacheck.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 数据校验注解
 * <p/>
 * 声明了@CheckModel类中所有什么@CheckApi注解的方法，会被动态注册到@Check的logic的语法中。
 * 
 * @author XuDL(Wisea)
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface CheckApi {
    /** 名称:默认和方法名一致 */
    public String name() default "";
}
