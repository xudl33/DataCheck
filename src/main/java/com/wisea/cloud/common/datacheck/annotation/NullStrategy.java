package com.wisea.cloud.common.datacheck.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 空值策略注解
 * 
 * @author XuDL(Wisea)
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface NullStrategy {
    /** 空值默认值 */
    public String value() default "";

    /** 跳过处理标志 默认:true */
    public boolean except() default true;

    /** 强制使用空值转换器 默认:false (如果属性与空值默认值都是String，就会直接使用，而不经过转换器，如果强制使用，就必须经过转换器的转换) */
    public boolean useExchager() default false;

    /** 空值转换器(可以将非String格式的空值默认值转换成其他格式) */
    public Class<? extends NullExchager> exchager() default NullExchager.class;
}
