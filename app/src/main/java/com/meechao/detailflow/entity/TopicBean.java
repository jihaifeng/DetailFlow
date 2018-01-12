package com.meechao.detailflow.entity;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-12-26 19:17
 * Mail：jihaifeng@meechao.com
 */
public class TopicBean {

  // s = #天气[1|话题]#
  public static final String REG_ID = "(?<=\\[)(\\d+)(?=|)"; // 获取标签id ，如 1
  public static final String REG_TEXT = "(?<=#)(.+)(?=\\[)"; // 获取 标签类别 ，如 话题
  public static final String REG_TYPE = "(?<=\\|)(.+)(?=\\|)"; // 获取 标签内容，不包括前后# ,如 天气

  public int labelTopicId; // 话题标签ID
  public int labelTopicType; // 话题标签类型 1=心得正文 2=心得提问 3=心得收藏时点评
  public String labelTopicValue; // 话题标签名称  #天气[1|话题]#

}
