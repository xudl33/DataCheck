package com.wisea.cloud.common.datacheck.util;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.collect.Lists;
import com.wisea.cloud.common.datacheck.annotation.Check;
import com.wisea.cloud.common.datacheck.entity.DataCheck;
import com.wisea.cloud.common.datacheck.entity.DataCheckModel;
import com.wisea.cloud.common.util.ConverterUtil;

/**
 * 数据校验工具类
 * 
 * @author XuDL(Wisea)
 *
 *         2018年1月18日 下午3:40:13
 */
public class DataCheckUtil implements ApplicationContextAware {
    /**
     * 日志对象
     */
    public static Logger logger = LoggerFactory.getLogger(DataCheckUtil.class);
    /** 校验上下文 */
    private static ThreadLocal<EvaluationContext> threadLocalContext = new NamedThreadLocal<>("ThreadLocalCheck");
    /** 校验模型 */
    private static DataCheckModel checkModel;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        checkModel = applicationContext.getBean(DataCheckModel.class);
    }

    /**
     * 校验并返回校验结果
     * 
     * @param data
     * @return
     */
    public static boolean check(Object data, Map<String, DataCheck> checkMap) {
        if (ConverterUtil.isNotEmpty(checkResultMsg(data, checkMap))) {
            return false;
        }
        return true;
    }

    /**
     * 校验并返回校验结果
     * 
     * @param data
     * @return
     */
    public static boolean check(Object data) {
        if (ConverterUtil.isNotEmpty(checkResultMsg(data))) {
            return false;
        }
        return true;
    }

    /**
     * 校验并返回校验消息
     * 
     * @param data
     *            entity
     * @param checkMap
     *            属性名与datacheck的map
     * @return
     */
    public static List<String> checkResultMsg(Object data, Map<String, DataCheck> checkMap) {
        List<String> errorMsg = Lists.newArrayList();
        if (ConverterUtil.isEmpty(data)) {
            return errorMsg;
        }
        if (ConverterUtil.isNotEmpty(data, checkMap)) {
            try {
                // 校验以list为方法参数的
                if (data instanceof List) {
                    List<?> objList = (List<?>) data;
                    for (Object obj : objList) {
                        errorMsg.addAll(checkResultMsg(obj, checkMap));
                    }
                } else if (data instanceof Map) {
                    // 校验以Map为方法参数的
                    Map<?, ?> objMap = (Map<?, ?>) data;
                    for (Object obj : objMap.keySet()) {
                        errorMsg.addAll(checkResultMsg(obj, checkMap));
                    }
                } else {
                    Map<String, Object> fieldMap = ConverterUtil.getAllFieldsMap(data);
                    for (String key : checkMap.keySet()) {
                        if (fieldMap.containsKey(key)) {
                            DataCheck req = checkMap.get(key);
                            Object val = fieldMap.get(key);
                            // 校验以list为方法参数的
                            if (val instanceof List) {
                                List<?> objList = (List<?>) val;
                                // 实际上这样的话有问题，list中的属性就无法重复了，如果相同 只能使用同一种规则
                                for (Object obj : objList) {
                                    errorMsg.addAll(checkResultMsg(obj, checkMap));
                                }
                            } else if (val instanceof Map) {
                                // 校验以Map为方法参数的
                                Map<?, ?> objMap = (Map<?, ?>) val;
                                // 实际上这样的话有问题，list中的属性就无法重复了，如果相同 只能使用同一种规则
                                for (Object obj : objMap.keySet()) {
                                    errorMsg.addAll(checkResultMsg(obj, checkMap));
                                }
                            } else {
                                errorMsg.addAll(checkOneField(key, val, req));
                            }
                        } // end fieldMap contains key
                    } // end checkMap forEach
                }
            } catch (Exception e) {
                // 使用框架中统一的异常处理，包装一层runtime异常是为了此处不抛错
                throw new RuntimeException(e);
            }
        }
        return errorMsg;
    }

    /**
     * 校验一个属性
     * 
     * @param feildName
     *            属性名
     * @param val
     *            值
     * @param req
     *            DataCheck
     * @return
     */
    public static List<String> checkOneField(String feildName, Object val, DataCheck req) {
        List<String> errorMsg = Lists.newArrayList();
        String[] tests = req.getTest();
        if (null != tests && tests.length > 0) {
            List<String> testList = Arrays.asList(tests);
            testList.forEach(t -> {
                if (t.equalsIgnoreCase("required")) {
                    // 为空则添加到ErrorMsg
                    if (ConverterUtil.isEmpty(val)) {
                        errorMsg.add(MessageFormat.format(req.getRequiredMsg(), feildName));
                    }
                }
                if (t.equalsIgnoreCase("length")) {
                    int length = req.getLength();
                    int mixLength = req.getMixLength();
                    if (ConverterUtil.isNotEmpty(length) && length > 0) {
                        // 长度等于
                        if (ConverterUtil.toString(val, "").length() != length) {
                            errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "标准", length));
                        }
                    }
                    if (ConverterUtil.isNotEmpty(mixLength) && mixLength > 0) {
                        // 混合长度等于
                        if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) != mixLength) {
                            errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "标准", mixLength));
                        }
                    }
                }
                if (t.equalsIgnoreCase("minLength")) {
                    int length = req.getLength();
                    int mixLength = req.getMixLength();
                    if (ConverterUtil.isNotEmpty(length) && length > 0) {
                        // 最小长度
                        if (ConverterUtil.toString(val, "").length() < length) {
                            errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最小", length));
                        }
                    }
                    if (ConverterUtil.isNotEmpty(mixLength) && mixLength > 0) {
                        // 最小混合长度
                        if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) < mixLength) {
                            errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最小", mixLength));
                        }
                    }
                }
                if (t.equalsIgnoreCase("maxLength")) {
                    int length = req.getLength();
                    int mixLength = req.getMixLength();
                    if (ConverterUtil.isNotEmpty(length) && length > 0) {
                        // 最大长度
                        if (ConverterUtil.toString(val, "").length() > length) {
                            errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最大", length));
                        }
                    }
                    if (ConverterUtil.isNotEmpty(mixLength) && mixLength > 0) {
                        // 最大混合长度
                        if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) > mixLength) {
                            errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最大", mixLength));
                        }
                    }
                }
                if (t.equalsIgnoreCase("liveable")) {
                    String[] liveables = req.getLiveable();
                    if (null != liveables && liveables.length > 0) {
                        boolean exixts = false;
                        for (String live : liveables) {
                            if (live.equals(ConverterUtil.toString(val))) {
                                exixts = true;
                                break;
                            }
                        }
                        // 不存在则添加到ErrorMsg
                        if (!exixts) {
                            errorMsg.add(MessageFormat.format(req.getLiveableMsg(), feildName));
                        }
                    }
                }
                if (t.equalsIgnoreCase("regex")) {
                    String[] regs = req.getRegex();
                    if (null != regs && regs.length > 0) {
                        boolean check = true;
                        for (String reg : regs) {
                            // 正则校验
                            Pattern pattern = Pattern.compile(reg);
                            Matcher matcher = pattern.matcher(ConverterUtil.toString(val, ""));
                            // 校验若有一个不通过就返回
                            if (!matcher.find()) {
                                check = false;
                                break;
                            }
                        }
                        // 校验不通过添加到ErrorMsg
                        if (!check) {
                            errorMsg.add(MessageFormat.format(req.getRegexMsg(), feildName));
                        }
                    }
                }
            }); // end testList forEach

            // 多个lengthCheck
            String testStr = testList.stream().collect(Collectors.joining(",")).toLowerCase();
            Integer[] lenRange = req.getLengthRange();
            Integer[] mixRange = req.getMixLengthRange();
            if (testStr.contains("minlength") && testStr.contains("maxlength")) {
                if (null != lenRange && lenRange.length >= 2) {
                    // 最小长度
                    if (ConverterUtil.toString(val, "").length() < lenRange[0]) {
                        errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最小", lenRange[0]));
                    }
                    // 最大长度
                    if (ConverterUtil.toString(val, "").length() > lenRange[1]) {
                        errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最大", lenRange[1]));
                    }
                }
                if (null != mixRange && mixRange.length >= 2) {
                    // 最小混合长度
                    if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) < mixRange[0]) {
                        errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最小", mixRange[0]));
                    }
                    // 最大混合长度
                    if (ConverterUtil.toString(val, "").length() > lenRange[1]) {
                        errorMsg.add(MessageFormat.format(req.getLengthMsg(), feildName, "最大", mixRange[1]));
                    }
                }
            } // end min&max length check

        } // end tests not empty

        return errorMsg;
    }

    @SuppressWarnings("unchecked")
    private static void setParents(StandardEvaluationContext context, Map<String, Object> fieldMap) {
        Object parent = fieldMap.get("parent");
        if (ConverterUtil.isNotEmpty(parent)) {
            context.setVariable("parent", fieldMap);
            Map<String, Object> parentMap = (Map<String, Object>) parent;
            if (parentMap.containsKey("parent")) {
                setParents(context, parentMap);
            }
        }
    }

    /**
     * 初始化校验上下文
     * 
     * @param data
     */
    private static void loadContext(Object data) {
        // 获取全部属性
        Map<String, Object> fieldMap = ConverterUtil.getAllFieldsMap(data);
        EvaluationContext parentContext = threadLocalContext.get();
        // 构建上下文
        if (ConverterUtil.isNotEmpty(parentContext)) {
            fieldMap.put("parent", parentContext.getRootObject().getValue());
        }
        // 新建上下文
        StandardEvaluationContext context = new StandardEvaluationContext(fieldMap);
        // 设置上下文父节点
        setParents(context, fieldMap);

        try {
            // 为上下文设置全局函数
            context.registerFunction("isEmpty", ConverterUtil.class.getDeclaredMethod("isEmpty", new Class[] { Object[].class }));
            context.registerFunction("isNotEmpty", ConverterUtil.class.getDeclaredMethod("isNotEmpty", new Class[] { Object[].class }));
            // 如果模型中有其他方法，也需要注册
            if (ConverterUtil.isNotEmpty(checkModel)) {
                checkModel.getConfig().forEach((k, v) -> {
                    context.registerFunction(k, v);
                });
            }
        } catch (Exception e) {
            logger.debug("exception in DataCheck with logic", e);
        }
        // 将所有属性赋值给上下文
        context.setVariables(fieldMap);
        // fieldMap.forEach((k, v) -> {
        // context.setVariable(k, v);
        // });
        // 设置到当前线程变量
        threadLocalContext.set(context);
    }

    /**
     * 错误消息转换
     * 
     * @param msgList
     *            错误消息
     * @param prefix
     *            前缀
     * @return
     */
    private static List<String> changeMsgList(List<String> msgList, String prefix) {
        if (ConverterUtil.isNotEmpty(msgList, prefix)) {
            return msgList.stream().map(i -> prefix + i).collect(Collectors.toList());
        }
        return msgList;
    }

    /**
     * 校验并返回校验消息
     * 
     * @param data
     * @return
     */
    public static List<String> checkResultMsg(Object data) {
        List<String> errorMsg = Lists.newArrayList();
        if (ConverterUtil.isEmpty(data)) {
            return errorMsg;
        }
        try {
            // 校验以list为方法参数的
            if (data instanceof List) {
                List<?> objList = (List<?>) data;
                for (int i = 0; i < objList.size(); i++) {
                    List<String> msgList = checkResultMsg(objList.get(i));
                    // list前缀
                    errorMsg.addAll(changeMsgList(msgList, "[" + i + "]."));
                }
            } else if (data instanceof Map) {
                // 校验以Map为方法参数的
                Map<?, ?> objMap = (Map<?, ?>) data;
                for (Object obj : objMap.keySet()) {
                    List<String> msgList = checkResultMsg(objMap.get(obj));
                    errorMsg.addAll(changeMsgList(msgList, "[" + obj + "]."));
                }
            } else {
                // 初始化校验上下文
                loadContext(data);

                Class<?> clazz = data.getClass();
                // 取得全部属性
                Field[] fields = ConverterUtil.getAllFields(clazz);
                for (Field field : fields) {
                    // 取得check注解
                    Check req = field.getDeclaredAnnotation(Check.class);
                    // 如果没有校验注解则跳过
                    if (ConverterUtil.isEmpty(req)) {
                        continue;
                    }
                    // 设置属性可以访问
                    String feildName = field.getName();
                    field.setAccessible(true);
                    Object val = field.get(data);
                    // 校验以list为方法参数的
                    if (val instanceof List) {
                        List<?> objList = (List<?>) val;
                        for (Object obj : objList) {
                            errorMsg.addAll(checkResultMsg(obj));
                        }
                    } else if (data instanceof Map) {
                        // 校验以Map为方法参数的
                        Map<?, ?> objMap = (Map<?, ?>) val;
                        for (Object obj : objMap.keySet()) {
                            errorMsg.addAll(checkResultMsg(obj));
                        }
                    } else {
                        errorMsg.addAll(checkOneField(feildName, val, req));
                        // 级联校验
                        if (req.cascade()) {
                            // 需要临时保存当前的上下文，因为级联时会被覆盖设置
                            EvaluationContext thisContext = threadLocalContext.get();
                            List<String> msgList = checkResultMsg(val);
                            errorMsg.addAll(changeMsgList(msgList, feildName + "."));
                            // 设置到当前线程变量
                            threadLocalContext.set(thisContext);
                        }
                    }
                } // end field forEach
            }
        } catch (Exception e) {
            // 使用框架中统一的异常处理，包装一层runtime异常是为了此处不抛错
            throw new RuntimeException(e);
        }
        return errorMsg;
    }

    /**
     * 校验一个field
     * 
     * @param feildName
     *            属性名
     * @param val
     *            值
     * @param req
     *            Check注解
     * @return
     */
    public static List<String> checkOneField(String feildName, Object val, Check req) {
        List<String> errorMsg = Lists.newArrayList();
        // 设置了check的进行相关校验
        if (ConverterUtil.isNotEmpty(req)) {
            String[] tests = req.test();
            if (null != tests && tests.length > 0) {
                List<String> testList = Arrays.asList(tests);
                testList.forEach(t -> {
                    if (t.equalsIgnoreCase("required")) {
                        // 为空则添加到ErrorMsg
                        if (ConverterUtil.isEmpty(val)) {
                            errorMsg.add(MessageFormat.format(req.requiredMsg(), feildName));
                        }
                    }
                    if (t.equalsIgnoreCase("length")) {
                        int length = req.length();
                        int mixLength = req.mixLength();
                        if (ConverterUtil.isNotEmpty(length) && length > 0) {
                            // 长度等于
                            if (ConverterUtil.toString(val, "").length() != length) {
                                errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "标准", length));
                            }
                        }
                        if (ConverterUtil.isNotEmpty(mixLength) && mixLength > 0) {
                            // 混合长度等于
                            if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) != mixLength) {
                                errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "标准", mixLength));
                            }
                        }
                    }
                    if (t.equalsIgnoreCase("minLength")) {
                        int length = req.length();
                        int mixLength = req.mixLength();
                        if (ConverterUtil.isNotEmpty(length) && length > 0) {
                            // 最小长度
                            if (ConverterUtil.toString(val, "").length() < length) {
                                errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最小", length));
                            }
                        }
                        if (ConverterUtil.isNotEmpty(mixLength) && mixLength > 0) {
                            // 最小混合长度
                            if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) < mixLength) {
                                errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最小", mixLength));
                            }
                        }
                    }
                    if (t.equalsIgnoreCase("maxLength")) {
                        int length = req.length();
                        int mixLength = req.mixLength();
                        if (ConverterUtil.isNotEmpty(length) && length > 0) {
                            // 最大长度
                            if (ConverterUtil.toString(val, "").length() > length) {
                                errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最大", length));
                            }
                        }
                        if (ConverterUtil.isNotEmpty(mixLength) && mixLength > 0) {
                            // 最大混合长度
                            if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) > mixLength) {
                                errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最大", mixLength));
                            }
                        }
                    }
                    if (t.equalsIgnoreCase("liveable")) {
                        String[] liveables = req.liveable();
                        if (null != liveables && liveables.length > 0) {
                            boolean exixts = false;
                            for (String live : liveables) {
                                if (live.equals(ConverterUtil.toString(val))) {
                                    exixts = true;
                                    break;
                                }
                            }
                            // 不存在则添加到ErrorMsg
                            if (!exixts) {
                                errorMsg.add(MessageFormat.format(req.liveableMsg(), feildName));
                            }
                        }
                    }
                    if (t.equalsIgnoreCase("regex")) {
                        String[] regs = req.regex();
                        if (null != regs && regs.length > 0) {
                            boolean check = true;
                            for (String reg : regs) {
                                // 正则校验
                                Pattern pattern = Pattern.compile(reg);
                                Matcher matcher = pattern.matcher(ConverterUtil.toString(val, ""));
                                // 校验若有一个不通过就返回
                                if (!matcher.find()) {
                                    check = false;
                                    break;
                                }
                            }
                            // 校验不通过添加到ErrorMsg
                            if (!check) {
                                errorMsg.add(MessageFormat.format(req.regexMsg(), feildName));
                            }
                        }
                    }
                    // 逻辑表达式校验
                    if (t.equalsIgnoreCase("logic")) {
                        boolean check = true;
                        check = logicCheck(req);
                        // 校验不通过添加到ErrorMsg
                        if (!check) {
                            errorMsg.add(MessageFormat.format(req.logicMsg(), feildName));
                        }
                    }
                });

                // 多个lengthCheck
                String testStr = testList.stream().collect(Collectors.joining(",")).toLowerCase();
                int[] lenRange = req.lengthRange();
                int[] mixRange = req.mixLengthRange();
                if (testStr.contains("minlength") && testStr.contains("maxlength")) {
                    if (null != lenRange && lenRange.length >= 2) {
                        // 最小长度
                        if (ConverterUtil.toString(val, "").length() < lenRange[0]) {
                            errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最小", lenRange[0]));
                        }
                        // 最大长度
                        if (ConverterUtil.toString(val, "").length() > lenRange[1]) {
                            errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最大", lenRange[1]));
                        }
                    }
                    if (null != mixRange && mixRange.length >= 2) {
                        // 最小混合长度
                        if (ConverterUtil.getMixLength(ConverterUtil.toString(val, "")) < mixRange[0]) {
                            errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最小", mixRange[0]));
                        }
                        // 最大混合长度
                        if (ConverterUtil.toString(val, "").length() > lenRange[1]) {
                            errorMsg.add(MessageFormat.format(req.lengthMsg(), feildName, "最大", mixRange[1]));
                        }
                    }
                } // end min&max length check

            } // end tests not empty
        } // end ConverterUtil.isNotEmpty(req) if
        return errorMsg;
    }

    /**
     * 进行逻辑校验
     * 
     * @param req
     * @return
     */
    public static boolean logicCheck(Check req) {
        String spel = req.logic();
        if (ConverterUtil.isNotEmpty(spel)) {
            try {
                EvaluationContext context = threadLocalContext.get();
                // 构建解析器
                ExpressionParser parser = new SpelExpressionParser();
                // 进行表达式校验
                return parser.parseExpression(spel).getValue(context, Boolean.class);
            } catch (Exception e) {
                logger.debug("exception in DataCheck with logic", e);
                return true;
            }
        }
        return true;
    }

}
