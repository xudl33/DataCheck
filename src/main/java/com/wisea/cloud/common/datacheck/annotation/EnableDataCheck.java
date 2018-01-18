package com.wisea.cloud.common.datacheck.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.wisea.cloud.common.datacheck.DataCheckAutoConfiguration;

/**
 * 开启DataCheck功能
 * 
 * @author XuDL(Wisea)
 *
 *         2018年1月18日 下午4:14:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DataCheckAutoConfiguration.class)
public @interface EnableDataCheck {

}
