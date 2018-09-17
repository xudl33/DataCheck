package com.wisea.cloud.common.datacheck.entity;

import java.lang.annotation.Annotation;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.wisea.cloud.common.datacheck.annotation.Check;
import com.wisea.cloud.common.datacheck.util.ConverterUtil;

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

    /** 校验类型 required:非空,length标准长度,minLength:最小长度,maxLength:最大长度,liveable:有效性,regex:正则表达式,logic:逻辑表达式 */
    private String[] test;

    /** 级联校验 若被校验的对象是一个bean, 默认不会校验对应的属性,若cascade=true,这会对其进行校验 */
    private boolean cascade = false;

    /** 空值跳过校验 若被校验值只有非空时再进行校验，则可以使用nullSkip=true */
    public boolean nullSkip = false;

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

    /** 逻辑表达式(test包含logic生效) */
    private String logic;

    /** 非空校验返回消息(test包含required生效) */
    private String requiredMsg = "{0}不能为空";

    /** 数据长度校验返回消息(test包含minLength或maxLength生效) */
    private String lengthMsg = "{0}{1}长度为{2}";

    /** 数据有效性校验返回消息(test包含liveable生效) */
    private String liveableMsg = "{0}数据无效";

    /** 正则表达式校验返回消息(test包含regex生效) */
    private String regexMsg = "{0}格式不正确";

    /** 逻辑表达式校验返回消息(test包含logic生效) */
    private String logicMsg = "{0}条件不正确";

    /** Check注解 */
    @JsonIgnore
    private Check annotation = null;

    public DataCheck() {

    }

    public DataCheck(DataCheckBuilder builder) {
        if (ConverterUtil.isNotEmpty(builder)) {
            ConverterUtil.copyProperties(builder, this);
        }
    }

    public DataCheck(String[] test) {
        this.test = test;
    }

    public DataCheck(String[] test, Integer length) {
        this.test = test;
        this.length = length;
    }

    public DataCheck(String[] test, Integer length, Integer mixLength) {
        this.test = test;
        this.length = length;
        this.mixLength = mixLength;
    }

    public DataCheck(String[] test, Integer length, Integer mixLength, String[] liveable) {
        this.test = test;
        this.length = length;
        this.mixLength = mixLength;
        this.liveable = liveable;
    }

    public DataCheck(String[] test, Integer length, Integer mixLength, String[] liveable, String[] regex) {
        this.test = test;
        this.length = length;
        this.mixLength = mixLength;
        this.liveable = liveable;
        this.regex = regex;
    }

    public DataCheck(String[] test, Integer length, Integer mixLength, String[] liveable, String[] regex, String logic) {
        this.test = test;
        this.length = length;
        this.mixLength = mixLength;
        this.liveable = liveable;
        this.regex = regex;
        this.logic = logic;
    }

    public DataCheck(String[] test, Boolean cascade, Integer length, Integer mixLength, String[] liveable, String[] regex, String logic) {
        this.test = test;
        this.cascade = cascade;
        this.length = length;
        this.mixLength = mixLength;
        this.liveable = liveable;
        this.regex = regex;
        this.logic = logic;
    }

    public DataCheck(String[] test, Boolean cascade, Boolean nullSkip, Integer length, Integer mixLength, String[] liveable, String[] regex, String logic) {
        this.test = test;
        this.cascade = cascade;
        this.nullSkip = nullSkip;
        this.length = length;
        this.mixLength = mixLength;
        this.liveable = liveable;
        this.regex = regex;
        this.logic = logic;
    }

    public String[] getTest() {
        return test;
    }

    public void setTest(String[] test) {
        this.test = test;
    }

    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(Boolean cascade) {
        this.cascade = cascade;
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

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getLogicMsg() {
        return logicMsg;
    }

    public void setLogicMsg(String logicMsg) {
        this.logicMsg = logicMsg;
    }

    public void setAnnotation(Check annotation) {
        this.annotation = annotation;
    }

    /**
     * 设置校验消息上下文
     * 
     * @param context
     */
    public void setMsgContext(String context) {
        this.setRequiredMsg(context + this.getRequiredMsg());
        this.setLengthMsg(context + this.getLengthMsg());
        this.setLiveableMsg(context + this.getLiveableMsg());
        this.setRegexMsg(context + this.getRegexMsg());
        this.setLogicMsg(context + this.getLogicMsg());
    }

    /**
     * 返回一个check注解实例
     * 
     * @return
     */
    public Check getAnnotation() {
        annotation = new Check() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Check.class;
            }

            @Override
            public String[] test() {
                return test;
            }

            @Override
            public String requiredMsg() {
                return requiredMsg;
            }

            @Override
            public String regexMsg() {
                return regexMsg;
            }

            @Override
            public String[] regex() {
                return regex;
            }

            @Override
            public int[] mixLengthRange() {
                int[] result = null;
                if (null != mixLengthRange) {
                    result = new int[mixLengthRange.length];
                    for (int i = 0; i < mixLengthRange.length; i++) {
                        result[i] = mixLengthRange[i];
                    }
                }
                return result;
            }

            @Override
            public int mixLength() {
                return mixLength;
            }

            @Override
            public String logicMsg() {
                return logicMsg;
            }

            @Override
            public String logic() {
                return logic;
            }

            @Override
            public String liveableMsg() {
                return liveableMsg;
            }

            @Override
            public String[] liveable() {
                return liveable;
            }

            @Override
            public int[] lengthRange() {
                int[] result = null;
                if (null != lengthRange) {
                    result = new int[lengthRange.length];
                    for (int i = 0; i < lengthRange.length; i++) {
                        result[i] = lengthRange[i];
                    }
                }
                return result;
            }

            @Override
            public String lengthMsg() {
                return lengthMsg;
            }

            @Override
            public int length() {
                return length;
            }

            @Override
            public boolean cascade() {
                return cascade;
            }

            @Override
            public boolean nullSkip() {
                return nullSkip;
            }
        };
        return annotation;
    }

    public static class DataCheckBuilder {
        /** 校验类型 required:非空,length标准长度,minLength:最小长度,maxLength:最大长度,liveable:有效性,regex:正则表达式,logic:逻辑表达式 */
        private String[] test;

        /** 级联校验 若被校验的对象是一个bean, 默认不会校验对应的属性,若cascade=true,这会对其进行校验 */
        private boolean cascade = false;

        /** 空值跳过校验 若被校验值只有非空时再进行校验，则可以使用nullSkip=true */
        public boolean nullSkip = false;

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

        /** 逻辑表达式(test包含logic生效) */
        private String logic;

        /** 非空校验返回消息(test包含required生效) */
        private String requiredMsg = "{0}不能为空";

        /** 数据长度校验返回消息(test包含minLength或maxLength生效) */
        private String lengthMsg = "{0}{1}长度为{2}";

        /** 数据有效性校验返回消息(test包含liveable生效) */
        private String liveableMsg = "{0}数据无效";

        /** 正则表达式校验返回消息(test包含regex生效) */
        private String regexMsg = "{0}格式不正确";

        /** 逻辑表达式校验返回消息(test包含logic生效) */
        private String logicMsg = "{0}条件不正确";

        private DataCheckBuilder() {
        }

        public static DataCheckBuilder newInstance() {
            return new DataCheckBuilder();
        }

        public DataCheckBuilder setCascade(boolean cascade) {
            this.cascade = cascade;
            return this;
        }

        public boolean isCascade() {
            return cascade;
        }

        public boolean isNullSkip() {
            return nullSkip;
        }

        public DataCheckBuilder setNullSkip(boolean nullSkip) {
            this.nullSkip = nullSkip;
            return this;
        }

        public DataCheckBuilder setLength(Integer length) {
            this.length = length;
            return this;
        }

        public DataCheckBuilder setLengthRange(Integer[] lengthRange) {
            this.lengthRange = lengthRange;
            return this;
        }

        public DataCheckBuilder setMixLength(Integer mixLength) {
            this.mixLength = mixLength;
            return this;
        }

        public DataCheckBuilder setMixLengthRange(Integer[] mixLengthRange) {
            this.mixLengthRange = mixLengthRange;
            return this;
        }

        public DataCheckBuilder setLiveable(String[] liveable) {
            this.liveable = liveable;
            return this;
        }

        public DataCheckBuilder setRegex(String[] regex) {
            this.regex = regex;
            return this;
        }

        public DataCheckBuilder setLogic(String logic) {
            this.logic = logic;
            return this;
        }

        public DataCheckBuilder setRequiredMsg(String requiredMsg) {
            this.requiredMsg = requiredMsg;
            return this;
        }

        public DataCheckBuilder setLengthMsg(String lengthMsg) {
            this.lengthMsg = lengthMsg;
            return this;
        }

        public DataCheckBuilder setLiveableMsg(String liveableMsg) {
            this.liveableMsg = liveableMsg;
            return this;
        }

        public DataCheckBuilder setRegexMsg(String regexMsg) {
            this.regexMsg = regexMsg;
            return this;
        }

        public DataCheckBuilder setLogicMsg(String logicMsg) {
            this.logicMsg = logicMsg;
            return this;
        }

        public DataCheckBuilder setTest(String... test) {
            this.test = test;
            return this;
        }

        public DataCheckBuilder addTest(String test) {
            if (null == this.test) {
                this.test = new String[] { test };
            } else {
                List<String> list = Lists.newArrayList(this.test);
                list.add(test);
                this.test = list.toArray(this.test);
            }
            return this;
        }

        public String[] getTest() {
            return test;
        }

        public Integer getLength() {
            return length;
        }

        public Integer[] getLengthRange() {
            return lengthRange;
        }

        public Integer getMixLength() {
            return mixLength;
        }

        public Integer[] getMixLengthRange() {
            return mixLengthRange;
        }

        public String[] getLiveable() {
            return liveable;
        }

        public String[] getRegex() {
            return regex;
        }

        public String getLogic() {
            return logic;
        }

        public String getRequiredMsg() {
            return requiredMsg;
        }

        public String getLengthMsg() {
            return lengthMsg;
        }

        public String getLiveableMsg() {
            return liveableMsg;
        }

        public String getRegexMsg() {
            return regexMsg;
        }

        public String getLogicMsg() {
            return logicMsg;
        }

        public DataCheck build() {
            return new DataCheck(this);
        }
    }
}
