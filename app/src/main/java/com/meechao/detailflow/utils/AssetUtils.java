package com.meechao.detailflow.utils;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.meechao.detailflow.App;
import com.meechao.detailflow.entity.CollectionTopic;
import com.meechao.detailflow.entity.EmojiBean;
import com.meechao.detailflow.entity.LivingLabelBean;
import com.meechao.detailflow.entity.TopicBean;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-12-26 18:13
 * Mail：jihaifeng@meechao.com
 */
public class AssetUtils {
  private static final String TAG = AssetUtils.class.getSimpleName().trim();

  public static List<String> getEmojiData() {
    String emojiJson = getAssetsData("emoji_list.json");
    if (TextUtils.isEmpty(emojiJson)) {
      return null;
    }
    EmojiBean bean = JSON.parseObject(emojiJson, EmojiBean.class);
    if (null == bean) {
      return null;
    }
    return bean.getEmoji_list();
  }

  public static List<LivingLabelBean> getLivingTag() {
    String tagData = getAssetsData("living_tag_json.json");
    if (TextUtils.isEmpty(tagData)) {
      return null;
    }
    List<LivingLabelBean> bean = JSON.parseArray(tagData, LivingLabelBean.class);
    if (null == bean) {
      return null;
    }
    return bean;
  }

  public static List<TopicBean> getArticleTopic() {
    String topicData = getAssetsData("article_topic.json");
    if (TextUtils.isEmpty(topicData)) {
      return null;
    }
    List<TopicBean> bean = JSON.parseArray(topicData, TopicBean.class);
    if (null == bean) {
      return null;
    }
    return bean;
  }

  public static List<CollectionTopic> getCollectionTopic() {
    String topicData = getAssetsData("collection_topic.json");
    if (TextUtils.isEmpty(topicData)) {
      return null;
    }
    List<CollectionTopic> bean = JSON.parseArray(topicData, CollectionTopic.class);
    if (null == bean) {
      return null;
    }
    return bean;
  }

  public static String getAssetsData(String assetsFileName) {
    InputStream is = null;
    ByteArrayOutputStream bos = null;
    try {
      is = App.getInstance().getApplicationContext().getAssets().open(assetsFileName);
      bos = new ByteArrayOutputStream();
      byte[] bytes = new byte[4 * 1024];
      int len;
      while ((len = is.read(bytes)) != -1) {
        bos.write(bytes, 0, len);
      }
      return new String(bos.toByteArray());
    } catch (Exception e) {
      e.printStackTrace();
      LogUtils.i(TAG, "getAssetsData Exception: " + e);
    } finally {
      try {
        if (is != null) {
          is.close();
        }
        if (bos != null) {
          bos.close();
        }
      } catch (IOException e) {
        LogUtils.i(TAG, "getAssetsData IOException: " + e);
      }
    }
    return null;
  }
}