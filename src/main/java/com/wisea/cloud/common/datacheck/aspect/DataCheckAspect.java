package com.wisea.cloud.common.datacheck.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.wisea.cloud.common.datacheck.annotation.Check;
import com.wisea.cloud.common.datacheck.util.DataCheckUtil;
import com.wisea.cloud.common.util.ConverterUtil;

/**
 * DataCheck数据校验切片
 * 
 * @author XuDL(Wisea)
 *
 *         2017年12月14日 下午5:41:14
 */
@Aspect
@Component
public class DataCheckAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 只要声明了@DataCheck的方法，都进行校验
     */
    @Pointcut("@annotation(com.wisea.cloud.common.datacheck.annotation.DataCheck)")
    public void dataCheck() {
    }

    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 在方法前校验@Check
     * 
     * @param joinPoint
     * @throws Throwable
     */
    @Before("dataCheck()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 获得切入方法参数
        Object[] args = joinPoint.getArgs();
        // // 获得切入目标对象
        // Object target = joinPoint.getThis();
        // 获得切入的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] params = method.getParameters();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        List<String> msgList = Lists.newArrayList();
        if (null != params) {
            for (int i = 0; i < params.length; i++) {
                Check checkAnn = params[i].getAnnotation(Check.class);
                if (ConverterUtil.isNotEmpty(checkAnn)) {
                    // 校验基本类型的
                    msgList.addAll(DataCheckUtil.checkOneField(parameterNames[i], args[i], checkAnn));
                } else {
                    // 校验以java bean对象 为方法参数的
                    msgList.addAll(DataCheckUtil.checkResultMsg(args[i]));
                }
            }
        }
        if (ConverterUtil.isNotEmpty(msgList)) {
            String msg = msgList.stream().collect(Collectors.joining(";"));
            logger.debug("DataCheck invalidate:" + msg);
            throw new RuntimeException(msg);
        }
    }
}
