package com.meechao.detailflow.utils;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.meechao.detailflow.App;
import com.meechao.detailflow.entity.EmojiBean;
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