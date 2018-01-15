package com.meechao.detailflow.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-11-03-0003 16:52
 * Mail：jihaifeng@meechao.com
 */
public class RegexUtils {

  // s = #天气[1|话题]#
  public static final String labelIdReg = "(?<=\\[)(\\d+)(?=|)"; // 获取标签id ，如 1
  public static final String topicIdReg = "(?<=\\[)(\\d+)(?=|)"; // 获取标签id ，如 1
  public static final String topicTypeReg = "(?<=\\|)(.+)(?=])"; // 获取 标签类别 ，如 话题
  public static final String textReg = "(?<=#)(.+)(?=\\[)"; // 获取 标签内容，不包括前后# ,如 天气
  public static final String topicValReg = "\\s#[^#]*?\\[.*?\\|话题\\]#\\s"; // 获取 标签内容 ,如 #天气[1|话题]#

  /**
   * 正则：手机号（简单）
   */
  public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";

  private RegexUtils() {
    throw new UnsupportedOperationException("please instantiate first.");
  }

  /**
   * 验证手机号（简单）
   *
   * @param input 待验证文本
   *
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isMobileSimple(final CharSequence input) {
    return isMatch(REGEX_MOBILE_SIMPLE, input);
  }

  /**
   * 判断是否匹配正则
   *
   * @param regex 正则表达式
   * @param input 要匹配的字符串
   *
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isMatch(final String regex, final CharSequence input) {
    return input != null && input.length() > 0 && Pattern.matches(regex, input);
  }

  /**
   * 正则提取内容
   *
   * @param str 文本内容
   * @param reg 正则表达式
   */
  public static List<String> getMathcherStr(String str, String reg) {
    List<String> result = new ArrayList<String>();
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(str);
    while (matcher.find()) {
      result.add(matcher.group());
    }
    System.out.println(result);
    return result;
  }

  /**
   * 正则提取第一条匹配到的内容
   *
   * @param str 文本内容
   * @param reg 正则表达式
   */
  public static String getFirstMathcherStr(String str, String reg) {
    String result = null;
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      result = matcher.group();
    }
    System.out.println(result);
    return result;
  }
}
