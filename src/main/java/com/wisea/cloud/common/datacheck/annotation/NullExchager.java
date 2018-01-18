package com.wisea.cloud.common.datacheck.annotation;

/**
 * 空值转换器
 * 
 * @author XuDL(Wisea)
 *
 *         2017年12月16日 下午1:07:04
 */
public interface NullExchager {
    /**
     * 将字符串转换为某类型
     * 
     * @param defaultVal
     *            默认值字符串
     * @return
     */
    public Object exchage(String defaultVal);
}
