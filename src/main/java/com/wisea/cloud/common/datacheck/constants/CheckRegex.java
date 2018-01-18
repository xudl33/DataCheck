package com.wisea.cloud.common.datacheck.constants;

/**
 * 常用正则整理
 * 
 * @author XuDL(Wisea)
 *
 *         2017年12月22日 下午2:24:22
 */
public class CheckRegex {
    /** 数字部分-start */
    /** 整数 */
    public static final String INTEGER = "^-?/d+$";

    /** 非负整数(正整数 + 0) */
    public static final String POSITIVE_INTEGER = "^/d+$";

    /** 非正整数(负整数 + 0) */
    public static final String MINUS_INTEGER = "^((-/d+)|(0+))$";

    /** 浮点数 */
    public static final String DECIMALS = "^(-?/d+)(/./d+)?$";
    /** 数字部分-end */

    /** 字母部分-start */
    /** 由26个英文字母组成的字符串 */
    public static final String ALPHABET = "^[A-Za-z]+$";

    /** 由26个英文字母的大写组成的字符串 */
    public static final String ALPHABET_UPPER = "^[A-Z]+$";

    /** 由26个英文字母的大写组成的字符串 */
    public static final String ALPHABET_LOWER = "^[a-z]+$";

    /** 由数字和26个英文字母组成的字符串 */
    public static final String NUMBER_ALPHABET = "^[A-Za-z0-9]+$";

    /** 由数字、26个英文字母或者下划线组成的字符串 */
    public static final String NUMBER_UNDERLINE_ALPHABET = "^/w+$";
    /** 字母部分-end */

    /** 特殊含义部分-start */
    /** email地址 */
    public static final String EMAIL = "^[/w-]+(/.[/w-]+)*@[/w-]+(/.[/w-]+)+$";

    /** 中文(中日韩统一表意文字) */
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]*$";
    /** 特殊含义部分-end */
}
