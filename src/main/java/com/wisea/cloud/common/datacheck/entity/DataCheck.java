package com.wisea.cloud.common.datacheck.entity;

/**
 * Check注解的Bean实现类
 * 
 * @author XuDL(Wisea)
 *
 * @date 2017年4月27日 下午2:36:17
 */
public class DataCheck {
    /** 用于设置长度限制的区分 */
    public static final int NONE = -1;
    /** 校验类型 required:非空,length标准长度,minLength:最小长度,maxLength:最大长度,liveable:有效性,regex:正则表达式 */
    private String[] test;

    /** 长度限制(test包含minLength或maxLength生效) */
    private Integer length = -1;

    /** 长度限制(test同时包含minLength和maxLength生效) */
    private Integer[] lengthRange;

    /** 中英文长度限制(test包含minLength或maxLength生效) */
    private Integer mixLength = -1;

    /** 中英文长度限制(test同时包含minLength和maxLength生效) */
    private Integer[] mixLengthRange;

    /** 数据有效性(test包含liveable生效) */
    private String[] liveable;

    /** 正则表达式(test包含regex生效) */
    private String[] regex;

    /** 非空校验返回消息(test包含required生效) */
    private String requiredMsg = "{0}不能为空";

    /** 数据长度校验返回消息(test包含minLength或maxLength生效) */
    private String lengthMsg = "{0}{1}长度为{2}";

    /** 数据有效性校验返回消息(test包含liveable生效) */
    private String liveableMsg = "{0}数据无效";

    /** 正则表达式校验返回消息(test包含regex生效) */
    private String regexMsg = "{0}格式不正确";

    public DataCheck() {

    }

    public DataCheck(String[] test) {
        this.test = test;
    }

    public DataCheck(String[] test, int length) {
        this.test = test;
        this.length = length;
    }

    public DataCheck(String[] test, int length, int mixLength) {
        this.test = test;
        this.length = length;
        this.mixLength = mixLength;
    }

    public DataCheck(String[] test, int length, int mixLength, String[] liveable) {
        this.test = test;
        this.length = length;
        this.mixLength = mixLength;
        this.liveable = liveable;
    }

    public DataCheck(String[] test, int length, int mixLength, String[] liveable, String[] regex) {
        this.test = test;
        this.length = length;
        this.mixLength = mixLength;
        this.liveable = liveable;
        this.regex = regex;
    }

    public String[] getTest() {
        return test;
    }

    public void setTest(String[] test) {
        this.test = test;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer[] getLengthRange() {
        return lengthRange;
    }

    public void setLengthRange(Integer[] lengthRange) {
        this.lengthRange = lengthRange;
    }

    public Integer getMixLength() {
        return mixLength;
    }

    public void setMixLength(Integer mixLength) {
        this.mixLength = mixLength;
    }

    public Integer[] getMixLengthRange() {
        return mixLengthRange;
    }

    public void setMixLengthRange(Integer[] mixLengthRange) {
        this.mixLengthRange = mixLengthRange;
    }

    public String[] getLiveable() {
        return liveable;
    }

    public void setLiveable(String[] liveable) {
        this.liveable = liveable;
    }

    public String[] getRegex() {
        return regex;
    }

    public void setRegex(String[] regex) {
        this.regex = regex;
    }

    public String getRequiredMsg() {
        return requiredMsg;
    }

    public void setRequiredMsg(String requiredMsg) {
        this.requiredMsg = requiredMsg;
    }

    public String getLengthMsg() {
        return lengthMsg;
    }

    public void setLengthMsg(String lengthMsg) {
        this.lengthMsg = lengthMsg;
    }

    public String getLiveableMsg() {
        return liveableMsg;
    }

    public void setLiveableMsg(String liveableMsg) {
        this.liveableMsg = liveableMsg;
    }

    public String getRegexMsg() {
        return regexMsg;
    }

    public void setRegexMsg(String regexMsg) {
        this.regexMsg = regexMsg;
    }

}
