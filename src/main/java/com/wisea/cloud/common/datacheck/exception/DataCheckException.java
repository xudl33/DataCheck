package com.wisea.cloud.common.datacheck.exception;

/**
 * 数据校验异常
 * 
 * @author XuDL(Wisea)
 *
 *         2018年1月18日 下午4:58:28
 */
public class DataCheckException extends RuntimeException {

    public DataCheckException(String msg) {
        super(msg);
    }

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 2410507475465317816L;

}
