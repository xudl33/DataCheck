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
 * 所有加了DataCheck注解的方法都会对参数进行@Check的校验
 * 
 * @author XuDL(Wisea)
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface DataCheck {
}
