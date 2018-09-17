package com.wisea.cloud.common.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import com.wisea.cloud.common.datacheck.util.ConverterUtil;

/**
 * 为boot方式增加ApplicationContext和相关方法
 * 
 * @author XuDL(Wisea)
 *
 *         2017年11月27日 下午2:04:49
 */
@Component
public class SpringBootContext implements ApplicationContextAware, ApplicationListener<ContextStartedEvent> {
    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(SpringBootContext.class);

    private static ApplicationContext applicationContext = null;

    /**
     * 非@import显式注入，@Component是必须的，且该类必须与main同包或子包 若非同包或子包，则需手动import 注入，有没有@Component都一样 可复制到Test同包测试
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;

        if (SpringBootContext.applicationContext != null) {
            logger.debug("applicationContext load completed");
        } else {
            throw new RuntimeException("loaded applicationContext error");
        }
    }

    /**
     * 获取applicationContext
     * 
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean
     * 
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        ApplicationContext context = getApplicationContext();
        if (ConverterUtil.isNotEmpty(context)) {
            return context.getBean(name);
        }
        return null;
    }

    /**
     * 通过class获取Bean
     * 
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        ApplicationContext context = getApplicationContext();
        if (ConverterUtil.isNotEmpty(context)) {
            return context.getBean(clazz);
        }
        return null;
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * 
     * @param name
     * @param clazz
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        ApplicationContext context = getApplicationContext();
        if (ConverterUtil.isNotEmpty(context)) {
            return context.getBean(name, clazz);
        }
        return null;
    }

    /**
     * 若application停止则要销毁
     * 
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        applicationContext = event.getApplicationContext();
    }

}