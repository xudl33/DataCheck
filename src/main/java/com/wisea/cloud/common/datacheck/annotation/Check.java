package com.wisea.cloud.common.datacheck.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验annotation
 * <p/>
 * 例:<br/>
 * '@Check(test="required")<br/>
 * '@Check(test="minLength", length = 10)<br/>
 * '@Check(test="liveable", liveable={"0","1"})<br/>
 * '@Check(test="regex", regex="[pdf|png|jpg|jpeg|bmp|gif]$")<br/>
 * '@Check(test={"required","regex"}, regex="[pdf|png|jpg|jpeg|bmp|gif]$")<br/>
 * 
 * @author XuDL(Wisea)
 *
 * @date 2017年4月24日 下午5:54:41
 */
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Check {

    /** 校验类型 required:非空,length标准长度,minLength:最小长度,maxLength:最大长度,liveable:有效性,regex:正则表达式,logic:逻辑表达式 */
    public String[] test();

    /** 级联校验 若被校验的对象是一个bean, 默认不会校验对应的属性,若cascade=true,这会对其进行校验 */
    public boolean cascade() default false;
    
    /** 空值跳过校验 若被校验值只有非空时再进行校验，则可以使用nullSkip=true */
    public boolean nullSkip() default false;

    /** 长度限制(test包含minLength或maxLength生效) */
    public int length() default -1;

    /** 长度限制(test同时包含minLength和maxLength生效) */
    public int[] lengthRange() default {};

    /** 中英文长度限制(test包含minLength或maxLength生效) */
    public int mixLength() default -1;

    /** 中英文长度限制(test同时包含minLength和maxLength生效) */
    public int[] mixLengthRange() default {};

    /** 数据有效性(test包含liveable生效) */
    public String[] liveable() default "";

    /** 逻辑表达式(test包含logic生效) */
    public String logic() default "";

    /** 正则表达式(test包含regex生效) */
    public String[] regex() default "";

    /** 非空校验返回消息(test包含required生效) */
    public String requiredMsg() default "{0}不能为空";

    /** 数据长度校验返回消息(test包含minLength或maxLength生效) */
    public String lengthMsg() default "{0}{1}长度为{2}";

    /** 数据有效性校验返回消息(test包含liveable生效) */
    public String liveableMsg() default "{0}数据无效";

    /** 正则表达式校验返回消息(test包含regex生效) */
    public String regexMsg() default "{0}格式不正确";

    /** 逻辑表达式校验返回消息(test包含logic生效) */
    public String logicMsg() default "{0}条件不正确";
}
